package com.tech.heathcilff.androidlib.net;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.tech.heathcilff.androidlib.cache.CacheManager;
import com.tech.heathcilff.androidlib.utils.BaseUtils;
import com.tech.heathcilff.androidlib.utils.CollectionUtils;
import com.tech.heathcilff.androidlib.utils.FrameConstants;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * network request
 * Created by zhangliang on 16/9/9.
 */
public class HttpRequest implements Runnable {
	// TODO ??? 不应该写死,而是从外部传入
	private final static String cookiePath = "/data/data/com.zhangweather/cache/cookie";

	// 区分get还是post的枚举
	private static final String REQUEST_GET = "get";
	private static final String REQUEST_POST = "post";

	private URLData urlData = null;
	private RequestCallback requestCallback = null;
	private List<RequestParameter> parameterList = null;
	private String url = null;
	private String newUrl = null;// url + parameters
	private Request okHttpRequest = null;
	private okhttp3.Response okHttpResponse = null;
	private OkHttpClient okHttpClient;

	private Handler handler;
	private ResponseJsonParser responseJsonParser;

	//新增的头信息
	HashMap<String, String> headers;

	// 服务器与APP之间的时差, 用于校准
	private static long deltaBetweenServerAndClientTime;

	public HttpRequest(final URLData data, final List<RequestParameter> params,
	                   final RequestCallback callBack) {
		this(data, params, callBack, null);
	}

	public HttpRequest(final URLData data, final List<RequestParameter> params,
	                   final RequestCallback callBack, ResponseJsonParser responseJsonParser) {
		if(data == null) {
			return;
		}
		urlData = data;

		url = urlData.getUrl();
		this.parameterList = params;
		requestCallback = callBack;
		this.responseJsonParser = responseJsonParser;

		if (okHttpClient == null) {
			okHttpClient = new OkHttpClient.Builder()
					.connectTimeout(10, TimeUnit.SECONDS)
					.readTimeout(10, TimeUnit.SECONDS)
					.writeTimeout(10, TimeUnit.SECONDS)
					.build();
		}

		handler = new Handler();

		headers = new HashMap<>();
	}


	/**
	 * 获取HttpUriRequest请求
	 */
	public Request getRequest() {
		return okHttpRequest;
	}

	@Override
	public void run() {
		// 构造request
		switch (urlData.getNetType()) {
			case REQUEST_GET:
				createGetRequest();// 分两种格式的请求
				break;
			case REQUEST_POST:
				createPostRequest();
				break;
			default:
				throw new IllegalArgumentException("unknown request type :" + urlData.getNetType());
		}

		// 添加header
		addHttpHeaders();

		// 添加Cookie到请求头中
		addCookie();

		if (newUrl != null) {
			Log.d("newUrl", newUrl);
		}
		logRequest();

		// response
		makeResponse();
	}

	private void createGetRequest() {
		if (parameterList != null && parameterList.size() > 0) {
			sortParameters();
			StringBuffer stringBuffer = new StringBuffer();
			for (RequestParameter p : parameterList) {
				if (stringBuffer.length() == 0) {
					stringBuffer.append(p.getName() + "=" + BaseUtils.UrlEncodeUnicode(p.getValue()));
				} else {
					stringBuffer.append("&" + p.getName() + "=" + BaseUtils.UrlEncodeUnicode(p.getValue()));
				}
			}
			newUrl = url + "?" + stringBuffer.toString();

		} else {
			newUrl = url;
		}

		//缓存判断(需要缓存则看是否有缓存)
		if (urlData.getExpires() > 0) {
			String content = CacheManager.getInstance().getFileCache(newUrl);
			Log.d("content", content + "");
			if (content != null) {
				handleNetworkOK(content);
				return;
			}
		}

		okHttpRequest = new Request.Builder().url(newUrl).build();
	}

	private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
	private void createPostRequest() {
		RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, parameterListToJson());
		okHttpRequest = new Request.Builder()
				.url(url)
				.post(requestBody)
				.build();
	}

	private String parameterListToJson() {
		if(CollectionUtils.isEmpty(parameterList)) {
			return "";
		} else {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("{");
			for(int i = 0, len = parameterList.size(); i < len; i ++) {
				RequestParameter parameter = parameterList.get(i);
				stringBuilder
						.append("'")
						.append(parameter.getName())
						.append("'")
						.append(":")
						.append("'")
						.append(parameter.getName())
						.append("'");
				if(i != len - 1) {
					stringBuilder.append(",");
				}
			}
			stringBuilder.append("}");
			return stringBuilder.toString();
		}
	}

	private void makeResponse() {

		try {
			// 响应
			okHttpResponse = okHttpClient.newCall(okHttpRequest).execute();
			logResponse(okHttpResponse);
			int statusCode = okHttpResponse.code();
			if (statusCode == 200) {
				updateDeltaBetweenServerAndClientTime();// 更新校准时间

				// 获取response string
				String strResponse = parseResponseToString();

				Log.d("strResponse", strResponse + "");
				String result = formatJsonResponse(strResponse);
				Log.d("strResponsef", result + "");

				// 回调 失败或成功
				if (requestCallback != null) {
					final Response responseInJson = JSON.parseObject(result, Response.class);
					if (responseInJson.hasError()) {
						handleNetworkError(responseInJson.getErrorMessage());
					} else {
						handleNetworkOK(responseInJson.getResult());
						// 是否缓存
						if (urlData.getNetType().equals(REQUEST_GET) && urlData.getExpires() > 0) {
							CacheManager.getInstance().putFileCache(newUrl, responseInJson.getResult(), urlData.getExpires());
						}
						// 保存cookie
						saveCookie();
					}
				}

			} else {
				handleNetworkError("网络异常" + statusCode);
			}

		} catch (IOException e) {
			e.printStackTrace();
			handleNetworkError("网络异常");
		}
	}

	// 根据gzip, 获取response string
	private String parseResponseToString() {
		if (okHttpResponse == null || okHttpResponse.body() == null) {
			return null;
		}

		String str = null;
		// 没有contentEncoding 或 没有gzip, 直接获取
		String contentEncoding = okHttpResponse.header("Content-Encoding");
		if (TextUtils.isEmpty(contentEncoding) || !contentEncoding.contains("gzip")) {
			try {
//				str = new String(okHttpResponse.body().bytes(), "UTF-8").trim();
				str = okHttpResponse.body().string();// TODO 可以针对不同的大小分别处理
			} catch (IOException e) {
				e.printStackTrace();
			}
			return str;
		}

		// 有gzip
		InputStream is = null;
		try {
			InputStream in = okHttpResponse.body().byteStream();
			Log.d("null ?", (in == null) + "");
			is = new GZIPInputStream(in);
			str = BaseUtils.stream2String(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			BaseUtils.closeIO(is);
		}
		return str;
	}


	private void handleNetworkError(final String errorMsg) {
		if (handler == null || requestCallback == null) {
			return;
		}
		// handler所在的线程(UI线程)
		handler.post(new Runnable() {
			@Override
			public void run() {
				requestCallback.onFail(errorMsg);
			}
		});
	}

	private void handleNetworkOK(final String result) {
		if (handler == null || requestCallback == null) {
			return;
		}
		// handler所在的线程(UI线程)
		handler.post(new Runnable() {
			@Override
			public void run() {
				requestCallback.onSuccess(result);
			}
		});
	}

	/**
	 * be sure url唯一
	 */
	private void sortParameters() {
		Collections.sort(parameterList, new Comparator<RequestParameter>() {
			/**
			 * @return parameter 1 比较char, 大则返1; 2 都相等则比较长度, 长则返回1; 3 相等返回0; 其余都返回-1
			 */
			@Override
			public int compare(@NonNull RequestParameter lhs, @NonNull RequestParameter rhs) {
				if (TextUtils.isEmpty(lhs.getName())) {
					return -1;
				}
				if (TextUtils.isEmpty(rhs.getName())) {
					return 1;
				}

				String p1 = lhs.getName().toUpperCase();
				String p2 = rhs.getName().toUpperCase();

				// 获取最小长度
				int minlength;
				int flag = 0;
				if (p1.length() > p2.length()) {
					minlength = p2.length();
					flag = 1;
				} else if (p1.length() < p2.length()) {
					minlength = p1.length();
					flag = -1;
				} else {
					minlength = p1.length();
					flag = 0;
				}

				// 比较
				char ch1, ch2;
				for (int i = 0; i < minlength; i++) {
					ch1 = p1.charAt(i);
					ch2 = p2.charAt(i);
					if (ch1 > ch2) {
						return 1;
					} else if (ch1 < ch2) {
						return -1;
					}
				}

				return flag;
			}
		});
	}

	/**
	 * 转换返回的数据为具有errorCode的格式的数据(伪装成从服务器返回的协议数据)
	 */
	private String formatJsonResponse(String result) {
		StringBuilder stringBuilder = new StringBuilder();

		boolean isError = false;
		int errorType = 0;
		String errorMessage = "''";
		String res = "";
		if (result == null || "".equals(result)) {
			isError = true;
			errorType = 1;
			errorMessage = "'暂无数据'";
			res = "''";
		} else {
//			int index1 = result.indexOf(":{");
//			int index2 = result.indexOf("}");
//			res = result.substring(index1 + 1, index2 + 1);
			if (responseJsonParser != null) {
				res = responseJsonParser.parse(result);
			}
		}

		stringBuilder.append("{");

		stringBuilder.append("'isError':");
		stringBuilder.append(isError);
		stringBuilder.append(",");

		stringBuilder.append("'errorType':");
		stringBuilder.append(errorType);
		stringBuilder.append(",");

		stringBuilder.append("'errorMessage':");
		stringBuilder.append(errorMessage);
		stringBuilder.append(",");

		stringBuilder.append("'result':");
		stringBuilder.append(result);
		stringBuilder.append("}");

		return stringBuilder.toString();
	}

	private synchronized void saveCookie() {
//		List<Cookie> cookies = httpClient.getCookieStore().getCookies();
//		List<SerializableCookie> serializableCookies = null;
//
//		if (!CollectionUtils.isEmpty(cookies)) {
//			serializableCookies = new ArrayList<>();
//			for (Cookie cookie : cookies) {
//				serializableCookies.add(new SerializableCookie(cookie));
//			}
//		}
//
//		BaseUtils.saveObject(cookiePath, serializableCookies);
	}

	// todo okhttp cookie
	private void addCookie() {
//		List<SerializableCookie> cookies = null;
//		Object object = BaseUtils.restoreObject(cookiePath);
//		if(object != null && object instanceof ArrayList<>) {
//			cookies = (ArrayList<SerializableCookie>) object;
//		}

//		if(!CollectionUtils.isEmpty(cookies)) {
//			BasicCookieStore basicCookieStore = new BasicCookieStore();
//			basicCookieStore.addCookies(cookies.toArray(new Cookie[]{}));
//			httpClient.setCookieStore(basicCookieStore);
//		} else {
//			httpClient.setCookieStore(null);
//		}
	}

	/**
	 * TODO 设置额外的头信息, 应该从外部传入, 如直接传入hashmap
	 */
	private void addHttpHeaders() {
		if(okHttpRequest == null || headers == null) {
			return;
		}
		okHttpRequest.newBuilder()
				.header(FrameConstants.ACCEPT_CHARSET, "UTF-8, *")
				.header(FrameConstants.USER_AGENT, "SimpleChinaWeather App");

//		headers.put(FrameConstants.ACCEPT_ENCODING, "gzip");
// TODO 返回的gzip文件一直有问题, unexpected end of gzip stream; java.io.EOFException
	}
	private void logRequest() {
		if (okHttpRequest == null) {
			return;
		}
		Log.d("log", "request header below-----");
		// TODO allHeaders size == 0 !!!
		// 可能是在执行过程中添加的, 但是手动添加的为什么没有添加上呢?
		logHeaders(okHttpRequest.headers());
	}

	private void logResponse(okhttp3.Response response) {
		if (response == null) {
			return;
		}
		Log.d("log", "response header below-----");
		logHeaders(okHttpResponse.headers());
	}
	private void logHeaders(Headers headers) {
		if (headers != null && headers.toMultimap() != null) {
			Set<Map.Entry<String, List<String>>> map = headers.toMultimap().entrySet();
			for(Map.Entry<String, List<String>> entry : map)
				Log.d(entry.getKey(), entry.getValue().toString());
		}
	}

	private SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);

	private void updateDeltaBetweenServerAndClientTime() {
		if (okHttpResponse == null) {
			return;
		}

		String serverDate =okHttpResponse.header("Date");//eg: Tue, 20 Sep 2016 07:09:45 GMT
		if (TextUtils.isEmpty(serverDate)) {
			return;
		}

		Date serverDateUAT = null;
		try {
			serverDateUAT = sdf.parse(serverDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return;
		}

		TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));// 设置系统时间为中国时间
		deltaBetweenServerAndClientTime = serverDateUAT.getTime() + 8 * 60 * 60 * 1000 - System.currentTimeMillis();

		Log.d("serverDateLong", String.valueOf(serverDateUAT.getTime()));
		Log.d("systemDateLong", String.valueOf(System.currentTimeMillis()));
	}


}
