package com.easemob.easeui.utils;

import android.util.Log;

import com.easemob.easeui.AppException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GsonParser {
	private static GsonParser gsonParser;

	private static Gson gson;

	private GsonParser() {

	}

	public static GsonParser getInstance() {
		if (gsonParser == null) {
			gsonParser = new GsonParser();
			gson = new Gson();
		}

		return gsonParser;
	}

	public <T> T getBean(String jsonString, Class<T> cls) throws AppException {
		T t = null;
		try {
			t = gson.fromJson(jsonString, cls);
		} catch (Exception e) {
			Log.d("errorInfo", "-->" + e.toString());
			throw  new AppException("数据解析异常", e);
		}
		return t;
	}

	public <T> T fromJson(String json, TypeToken<T> token) throws AppException {

		if (json==null||"".equals(json)) {
			return null;
		}

		try {
			return gson.fromJson(json, token.getType());
		} catch (Exception e) {
			throw  new AppException("数据解析异常", e);
		}

	}

	public String toJson(Object obj) {
		return gson.toJson(obj);
	}
}
