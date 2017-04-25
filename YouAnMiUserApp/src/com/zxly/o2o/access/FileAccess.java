package com.zxly.o2o.access;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.easemob.easeui.AppException;
import com.zxly.o2o.controller.AppController;
import com.zxly.o2o.util.StringUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


public class FileAccess {

	private static final String tag = "FileAccess";

	public static final String PARNET_PATH = "com.zxly.o2o";

	public static final String IMAGE_PATH = PARNET_PATH + "/image/";

	public static final String SOUND_PATH = PARNET_PATH + "/sound/";
	
	public static final String APK_PATH = PARNET_PATH + "/apk/";

	private static final String ENCODING = "UTF-8";



	
	private Context context;

	private File sdCardDir;

	public FileAccess() {
		this.context =AppController.getInstance().getTopAct();
		Log.i(tag, context.getFilesDir().getAbsolutePath());
		Log.i(tag, context.getCacheDir().getAbsolutePath());
	}

	
	public  void del(String delpath) {

		File file = new File(sdCardDir,delpath);
		// 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
		if(!file.exists())
			return;
		if (!file.isDirectory()) {
			file.delete();
		} else if (file.isDirectory()) {
			String[] filelist = file.list();
			for (int i = 0; i < filelist.length; i++) {
				File delfile = new File(sdCardDir,delpath + "/" + filelist[i]);
				if (!delfile.isDirectory()) {
					delfile.delete();
				} else if (delfile.isDirectory()) {
					del(delpath + "/" + filelist[i]);
				}
			}
			file.delete();
		}

	}
	public boolean checkSDCard() {
		boolean sdCard = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (sdCard)
			sdCardDir = Environment.getExternalStorageDirectory();
		return sdCard;
	}

	public File getSdCardDir() {
		return sdCardDir;
	}

	public File getSdCardFile(String name) {
		if (!checkSDCard())
			return null;
		File f = new File(sdCardDir, PARNET_PATH + "/" + name);
		File p = f.getParentFile();
		if (!p.exists())
			p.mkdirs();
		return f;
	}

	/**
	 * 保存私有文件内容
	 * 
	 * @param name
	 * @param lines
	 * @throws AppException 
	 */
	public void saveLocal(String name, List<String> lines, boolean append) throws AppException {
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					context.openFileOutput(name, append ? Context.MODE_APPEND
							: Context.MODE_PRIVATE), ENCODING));
			for (String line : lines) {
				out.write(line);
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			Log.e(tag, "saveLocal error", e);
			throw new AppException("saveLocal error", e);
		}
	}
	public void saveLocal(String name, String  lines, boolean append) throws AppException {
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					context.openFileOutput(name, append ? Context.MODE_APPEND
							: Context.MODE_PRIVATE), ENCODING));
				out.write(lines);
			
			out.close();
		} catch (IOException e) {
			Log.e(tag, "saveLocal error", e);
			throw new AppException("saveLocal error", e);
		}
	}

	private void saveLocal(byte[] bytes, String name) {
		try {
			
			OutputStream out = context.openFileOutput(name,
					Context.MODE_PRIVATE);
			out.write(bytes);
			out.close();
		} catch (IOException e) {
			Log.e(tag, "saveLocal error", e);
		}
	}

	/**
	 * 读取私有文件
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public List<String> readLocal(String fileName) {
		List<String> lines = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					context.openFileInput(fileName), ENCODING));
			String line = null;
			while ((line = in.readLine()) != null) {
				if (StringUtil.isNull(line))
					continue;
				lines.add(line);
			}
			in.close();
		} catch (IOException e) {
			Log.e(tag, "readLocal error", e);
		}
		return lines;
	}

	public List<String> readLocal(InputStream is) {
		List<String> lines = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(is,
					ENCODING));
			String line = null;
			while ((line = in.readLine()) != null) {
				if (StringUtil.isNull(line))
					continue;
				lines.add(line);
			}
			in.close();
		} catch (IOException e) {
			Log.e(tag, "readLocal error", e);
		}
		return lines;
	}

	public byte[] readLocalBytes(String fileName) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			byte[] buf = new byte[1024];
			InputStream  in = context.openFileInput(fileName);
			int count = -1;
			while ((count = in.read(buf)) != -1) {
				out.write(buf, 0, count);
			}
			in.close();
		} catch (IOException e) {
			Log.e(tag, "readLocal error", e);
		}
		return out.toByteArray();
	}

	public File getFile(String fileName, String path) {
		if (!checkSDCard()) {
			return readLocalFile(fileName);
		} else {
			return readSDCardFile(fileName, path);
		}
	}

	public File readLocalFile(String fileName) {
		return context.getFileStreamPath(fileName);
	}

	public File readSDCardFile(String fileName, String path) {
		File file = new File(sdCardDir, path + "/" + fileName);
		if (file.exists())
			return file;
		else
			return null;
	}

	public void clearCacheFile(String folder,String prefix) {
		if (!checkSDCard()) {
			String[] filenames = context.fileList();
			for (int i = 0; i < filenames.length; i++) {
				String filename = filenames[i];
				if (!StringUtil.isNull(filename)) {
					if (filename.startsWith(prefix)) {
						context.deleteFile(filename);
					}
				}
			}
		}
		else {
			// 有SD卡
			File file = new File(sdCardDir, folder);
			if (file.exists()) {
				File[] chirden = file.listFiles();
				if (null != chirden) {
					for (int i = 0; i < chirden.length; i++) {
						if (chirden[i].isFile()) {
							chirden[i].delete();
						}
					}
				}
			}
		}
	}
	
	/**
	 * 清除SD卡图片
	 */
	public void clearImage() {
		// SD卡不存在，清除內存中的文件
		if (!checkSDCard()) {
			String[] filenames = context.fileList();
			for (int i = 0; i < filenames.length; i++) {
				String filename = filenames[i];
				if (!StringUtil.isNull(filename)) {
					if (Character.isDigit(filename.charAt(0))
							&& (filename.endsWith(".png") || filename
									.endsWith(".jpg"))) {
						context.deleteFile(filename);
					}
				}
			}
		} else {
			// 有SD卡
			File file = new File(sdCardDir, IMAGE_PATH);
			if (file.exists()) {
				File[] chirden = file.listFiles();
				if (null != chirden) {
					for (int i = 0; i < chirden.length; i++) {
						if (chirden[i].isFile()) {
							chirden[i].delete();
						}
					}
				}
			}
		}

	}

	// 清理配置文件
	public void clearCfg() {
		String[] filenames = context.fileList();
		for (int i = 0; i < filenames.length; i++) {
			String filename = filenames[i];
			try {
				if ("vc".equalsIgnoreCase(filename)) {
					context.deleteFile(filename);
				} else {
					if (filename.indexOf('.') == -1) {
						Integer.parseInt(filename);
						context.deleteFile(filename);
					}
				}
			} catch (Exception e) {
				continue;
			}
		}
	}

	public void saveFile(byte[] bytes, String fileName, String path) {
		if (!checkSDCard()) {
			saveLocal(bytes, fileName);
			return;
		}
		fileName = path + fileName;
		try {
			File f = new File(sdCardDir, fileName);
			File p = f.getParentFile();
			if (!p.exists())
				p.mkdirs();
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(bytes);
			fos.close();
		} catch (Exception e) {
			Log.e(tag, "save sdcard error", e);
		}
	}

	public void saveApk(byte[] bytes,String fileName){
		saveFile(bytes, fileName, APK_PATH);
	}
	
	public void saveSound(byte[] bytes, String fileName) {
		saveFile(bytes, fileName, SOUND_PATH);
	}

	public void saveImage(byte[] bytes, String fileName) {
		saveFile(bytes, fileName, IMAGE_PATH);
	}

	public void saveData(byte[] bytes, String fileName, String folder) {
		saveFile(bytes, fileName, PARNET_PATH + "/" + folder + "/");
	}

	public File readFile(String fileName, String folder) {
		if (!checkSDCard()) {
			return context.getFileStreamPath(fileName);
		}
		fileName = PARNET_PATH + "/" + folder + "/" + fileName;
		return new File(sdCardDir, fileName);
	}

	public File readImage(String fileName) {
		if (!checkSDCard()) {
			return context.getFileStreamPath(fileName);
		}
		fileName = IMAGE_PATH + fileName;
		return new File(sdCardDir, fileName);
	}

	public List<String> readSdcard(String fileName) {
		if (!checkSDCard()) {
			return readLocal(fileName);
		}
		fileName = PARNET_PATH + "/" + fileName;
		File f = new File(sdCardDir, fileName);
		List<String> lines = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(f), ENCODING));
			String line = null;
			while ((line = in.readLine()) != null) {
				if (StringUtil.isNull(line))
					continue;
				lines.add(line);
			}
			in.close();
		} catch (IOException e) {
			Log.e(tag, "readSdcard error", e);
		}
		return lines;
	}

	public void saveSdcard(String fileName, List<String> lines, boolean append) throws AppException {
		if (!checkSDCard()) {
			saveLocal(fileName, lines, append);
			return;
		}
		fileName = PARNET_PATH + "/" + fileName;
		try {
			File f = new File(sdCardDir, fileName);
			File p = f.getParentFile();
			if (!p.exists())
				p.mkdirs();
			if (!f.exists())
				f.createNewFile();
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(f, append), ENCODING));
			for (String line : lines) {
				out.write(line);
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			Log.e(tag, "saveLocal error", e);
		}
	}
}
