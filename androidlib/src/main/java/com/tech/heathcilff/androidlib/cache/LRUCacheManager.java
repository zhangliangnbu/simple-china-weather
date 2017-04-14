package com.tech.heathcilff.androidlib.cache;

import com.tech.heathcilff.androidlib.utils.EncryptUtils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * 失效时间；替换策略
 * 内存缓存 + 文件缓存
 * Created by zhangliang on 02/03/2017.
 */

public class LRUCacheManager {

	private static final int ELEMENT_MAX_NUMBER = 10;// 个数限制 or 空间限制 // TODO: 02/03/2017
	private static List<CacheElement> list = new ArrayList<>();
	private static LRUCacheManager instance = new LRUCacheManager();

	private LRUCacheManager() {}

	public static LRUCacheManager instance() {
		return instance;
	}

	public void add(String key, Object o) {
		addElement(new CacheElement(key, o));
	}

	public boolean contains(String key) {
		for(CacheElement element : list) {
			if(element.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}

	public Object get(String key) {
		int index = list.indexOf(new CacheElement(key, null));
		if(index > -1) {
			return list.get(index).getData();
		}
		return null;
	}

	public synchronized void clear() {
		list.clear();
	}

	private synchronized void addElement(CacheElement cacheElement) {
		if(list.contains(cacheElement)) {
			int index = list.indexOf(cacheElement);
			list.get(index).setData(cacheElement.getData());
		} else {
			if(list.size() == ELEMENT_MAX_NUMBER) {
				// full, replacement strategy. new -> last, old -> first
				list.remove(0);// remove first
			}
			list.add(cacheElement);
		}
	}

	private synchronized void removeElement(CacheElement cacheElement) {
		int index = list.indexOf(cacheElement);
		if(index > -1) {
			list.remove(index);
		}
	}

	private String getMD5Key(String key) {
		String md5Key = "";
		try {
			md5Key = EncryptUtils.encryptMD5U32(key);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return md5Key;
	}


}
