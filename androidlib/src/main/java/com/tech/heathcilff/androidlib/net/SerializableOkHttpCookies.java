package com.tech.heathcilff.androidlib.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import okhttp3.Cookie;

/**
 *
 * Created by zhangliang on 06/03/2017.
 */

public class SerializableOkHttpCookies implements Serializable {

	private static final long serialVersionUID = -1506455271030590187L;

	private transient final Cookie cookies;// 不参与序列化
	private transient Cookie clientCookies;// 不参与序列化

	public SerializableOkHttpCookies(Cookie cookies) {
		this.cookies = cookies;
	}

	public Cookie getCookies() {
		return clientCookies == null ? cookies : clientCookies;
	}

	// 序列化
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeObject(cookies.name());
		out.writeObject(cookies.value());
		out.writeLong(cookies.expiresAt());
		out.writeObject(cookies.domain());
		out.writeObject(cookies.path());
		out.writeBoolean(cookies.secure());
		out.writeBoolean(cookies.httpOnly());
		out.writeBoolean(cookies.hostOnly());
		out.writeBoolean(cookies.persistent());
	}

	// 反序列化
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		String name = (String) in.readObject();
		String value = (String) in.readObject();
		long expiresAt = in.readLong();
		String domain = (String) in.readObject();
		String path = (String) in.readObject();
		boolean secure = in.readBoolean();
		boolean httpOnly = in.readBoolean();
		boolean hostOnly = in.readBoolean();

		Cookie.Builder builder = new Cookie.Builder();
		builder = builder.name(name);
		builder = builder.value(value);
		builder = builder.expiresAt(expiresAt);
		builder = hostOnly ? builder.hostOnlyDomain(domain) : builder.domain(domain);
		builder = builder.path(path);
		builder = secure ? builder.secure() : builder;
		builder = httpOnly ? builder.httpOnly() : builder;
		clientCookies =builder.build();
	}

}
