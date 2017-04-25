package com.zxly.o2o.util;

public class AppLog {

	public static final String TAG = "YouAnMiShop";

	public static boolean saveErrorLog = false;

	public static boolean DEBUG = true;

	public static void v(String msg) {
		if (DEBUG)
			android.util.Log.v(TAG, buildMessage(msg));
	}

	public static void v(String msg, Throwable thr) {
		if (DEBUG)
			android.util.Log.v(TAG, buildMessage(msg), thr);
	}

	public static void d(String msg) {
		if (DEBUG)
			android.util.Log.d(TAG, buildMessage(msg));
	}

	public static void d(String msg, Throwable thr) {
		if (DEBUG)
			android.util.Log.d(TAG, buildMessage(msg), thr);
	}

	public static void i(String msg) {
		if (DEBUG)
			android.util.Log.i(TAG, buildMessage(msg));
	}

	public static void i(String msg, Throwable thr) {
		if (DEBUG)
			android.util.Log.i(TAG, buildMessage(msg), thr);
	}

	public static void w(String msg) {
		android.util.Log.w(TAG, buildMessage(msg));
	}

	public static void w(String msg, Throwable thr) {
		if (DEBUG)
			android.util.Log.w(TAG, buildMessage(msg), thr);
	}

	public static void e(String msg) {
		if (DEBUG)
			android.util.Log.e(TAG, buildMessage(msg));
	}

	public static void e(String msg, Throwable thr) {
		if (DEBUG)
			android.util.Log.e(TAG, buildMessage(msg), thr);
	}

	public static void p(Throwable thr) {
		if (DEBUG)
			android.util.Log.w(TAG, "", thr);
	}

	public static void e(String tag, String msg) {
		if (DEBUG)
			android.util.Log.e(tag, msg);
	}

	public static void e(Throwable thr) {
		if (DEBUG)
			android.util.Log.e(TAG, "", thr);
	}

	public static void i(String tag, String msg) {
		if (DEBUG)
			android.util.Log.i(tag, msg);
	}

	public static void v(String tag, String msg) {
		if (DEBUG)
			android.util.Log.v(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (DEBUG)
			android.util.Log.d(tag, msg);
	}

	protected static String buildMessage(String msg) {
		StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];
		try {
			String name = caller.getClassName();
			return new StringBuilder()
					.append(name.substring(name.lastIndexOf(".") + 1, name.length())).append(".")
					.append(caller.getMethodName()).append("(): ").append(msg).toString();
		} catch (Exception e) {
		}
		return new StringBuilder().append(caller.getClassName()).append(".")
				.append(caller.getMethodName()).append("(): ").append(msg).toString();
	}

}