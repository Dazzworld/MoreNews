package com.dazzcoder.morenews.cache;

import java.io.Serializable;

/**
 * 缓存对象
 */
public class CacheObject implements Serializable {

	private static final long serialVersionUID = 1L;

	private Object data[];

	public CacheObject(Object data[]) {
		super();
		this.data = data;
	}

	public Object[] getData() {
		return data;
	}

}
