package com.zxly.o2o.application;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.zxly.o2o.util.Constants;
import com.zxly.o2o.util.FileUtils;
import com.zxly.o2o.util.ViewUtils;

/** 应用程序异常类：用于捕获异常和提示错误信息
 * 
 * @author stefan
 * @version 2.0.0 */
public class CrashHandler implements UncaughtExceptionHandler {

	public static final String TAG = "CrashHandler";
	private UncaughtExceptionHandler mDefaultHandler;
	private static CrashHandler INSTANCE = new CrashHandler();
	Handler handler = new Handler(Looper.getMainLooper());
	private Context mContext;
	private Map<String, String> infos = new HashMap<String, String>();
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		if (INSTANCE != null) {
			return INSTANCE;
		} else {
			INSTANCE = new CrashHandler();
		}
		return INSTANCE;
	}

	/** 初始化
	 *
	 * @param context */
	public void init(Context context) {
		mContext = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/** 当UncaughtException发生时会转入该函数来处理 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);
			} catch (Exception e) {
			}
			// 退出程序
			AppController.exitAct();
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}

	/** 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 *
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false. */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		handler.post(new Runnable() {

			@Override
			public void run() {
				ViewUtils.showToast( "很抱歉,程序出现异常,即将退出！");

			}
		});
			collectDeviceInfo(mContext);
			saveCrashInfo2File(ex);
		return true;
	}

	/** 收集设备参数信息
	 *
	 * @param ctx */
	public void collectDeviceInfo(Context ctx) {
		String versionName = AppController.mVersionName;
		String versionCode = AppController.mVersionCode + "";
		infos.put("versionName", versionName);
		infos.put("versionCode", versionCode);
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());

			} catch (Exception e) {
			}
		}
	}

	/** 保存错误信息到文件中
	 *
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器 */
	private String saveCrashInfo2File(Throwable ex) {

		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try {
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			String fileName = "/crash-" + time + "-" + timestamp + ".txt";
			if (FileUtils.hasSDCard()) {
				String path = Constants.STORE_CACHE_PATH+"/error";
				File file = new File( Environment.getExternalStorageDirectory(),path + fileName);
				File p=file.getParentFile();
				if (!p.exists()) {
					p.mkdirs();
				}
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(sb.toString().getBytes());
				fos.close();
			}
			return fileName;
		} catch (Exception e) {
			Log.e("--crash-error--", e.getMessage());
		}
		return null;
	}
}
