/**
 * Copyright(C)2012-2013 深圳市掌星立意科技有限公司版权所有
 * 创 建 人:Administrator
 * 修 改 人:
 * 创 建日期:2013-7-19
 * 描	   述:
 * 版 本 号:
 */
package com.zxly.o2o.util;

import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
public class SdHelper {
	public static long getSDAvailaleSize() {
		List<String> paths = getSDRealPath();
		long size = 0;
		if (paths != null && paths.size() != 0) {
			for (int i = 0; i < paths.size(); i++) {
				StatFs stat = new StatFs(paths.get(i));
				long blockSize = stat.getBlockSize();
				long availableBlocks = stat.getAvailableBlocks();
				size += availableBlocks * blockSize;
			}
		}
		return size;
	}

	public static long getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * 获取手机内部总的存储空间
	 * 
	 * @return
	 */
	public static long getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

	public static long getSdAllSize() {
		List<String> paths = getSDRealPath();
		long size = 0;
		if (paths != null && paths.size() != 0) {
			for (int i = 0; i < paths.size(); i++) {
				StatFs stat = new StatFs(paths.get(i));
				long blockSize = stat.getBlockSize();
				long availableBlocks = stat.getBlockCount();
				size += availableBlocks * blockSize;
			}
		}
		return size;
	}

	// 获取真实的SD卡路径 不包括挂在或者虚拟公用的部分
	// 计算SD卡容量的时候建议使用次方法 返回list长度为0时 说明没有真实的SD
	public static List<String> getSDRealPath() {
		List<String> list = new ArrayList<String>();
		try {
			String line;
			String path = Environment.getExternalStorageDirectory().getParent();
			BufferedReader br = new BufferedReader(new FileReader(
					"/proc/mounts"));
			while ((line = br.readLine()) != null) {
				if (line.contains("secure"))
					continue;
				if (line.contains("asec"))
					continue;
				if (line.contains("fat") || line.contains("ext4")) {
					String columns[] = line.split(" ");
					if (columns != null && columns.length > 1) {
						if (columns[1].startsWith(path)||columns[1].startsWith("/storage/extSdCard")) {
							list.add(columns[1]);
						}
					}
				}
			}
		} catch (Exception e) {
			AppLog.p(e);
		}

		return list;
	}

	// 获取全部的SD卡路径 包括挂在或者虚拟公用的部分
	// 单一SD卡 可以通过Environment.getExternalStorageDirectory() 获取 多SD卡手机需要次方法获取
	public static List<String> getExterPath() {
		List<String> list = new ArrayList<String>();
		try {
			String line;
			String path = Environment.getExternalStorageDirectory().getParent();
			BufferedReader br = new BufferedReader(new FileReader(
					"/proc/mounts"));
			while ((line = br.readLine()) != null) {
				if (line.contains("secure"))
					continue;
				if (line.contains("asec"))
					continue;
				if (line.contains("fat") || line.contains("ext4")||line.contains("fuse")) {
					String columns[] = line.split(" ");
					if (columns != null && columns.length > 1) {
						if (columns[1].startsWith(path)||columns[1].startsWith("/storage/extSdCard")) {
							list.add(columns[1]);
						}
					}
				} 
			}
		} catch (Exception e) {
			AppLog.p(e);
		}

		return list;
	}

	public static String computeSize(long sizeInByte, boolean isHas) {
		if (sizeInByte >= 1024 * 1024 * 1024) {
			if (isHas) {
				return String.format("%.2fGB", sizeInByte / 1073741824.0);
			} else {
				return String.format("%.2fG", sizeInByte / 1073741824.0);
			}
		}
		if (sizeInByte >= 1024 * 1024) {
			if (isHas) {
				return String.format("%.2fMB", sizeInByte / 1048576.0);
			} else {
				return String.format("%.0fM", sizeInByte / 1048576.0);
			}
		} else if (sizeInByte >= 1024) {
			if (isHas) {
				return String.format("%.2fKB", sizeInByte / 1024.0);
			} else {
				return String.format("%.0fK", sizeInByte / 1024.0);
			}
		} else {
			return String.format("%dB", sizeInByte);
		}
	}

	public static String computeSize(long sizeInByte) {
		return computeSize(sizeInByte, true);
	}

	public static boolean hasSdcard() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	public static String computeSizeNone(long sizeInByte) {
		if (sizeInByte >= 1024 * 1024 * 1024) {
			return String.format("%.2f", sizeInByte / 1073741824.0);
		}
		if (sizeInByte >= 1024 * 1024) {
			return String.format("%.2f", sizeInByte / 1048576.0);
		} else if (sizeInByte >= 1024) {
			return String.format("%.2f", sizeInByte / 1024.0);
		} else {
			return String.format("%d", sizeInByte);
		}
	}

}
