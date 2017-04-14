package com.tech.heathcilff.androidlib.cache;

import java.io.Serializable;

/**
 *
 * Created by zhangliang on 02/03/2017.
 */

public class CacheElement implements Serializable {
	private static final long serialVersionUID = -5179223151656434362L;
	private String key;
	private Object data;
	private int index;
	private int hitCount;

	public CacheElement(String key, Object data) {
		this.key = key;
		this.data = data;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CacheElement)) return false;

		CacheElement that = (CacheElement) o;

		return key != null ? key.equals(that.key) : that.key == null;

	}

	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getHitCount() {
		return hitCount;
	}

	public void setHitCount(int hitCount) {
		this.hitCount = hitCount;
	}
}
