package com.shyz.downloadutil;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Transient;
import com.lidroid.xutils.http.HttpHandler;

import java.io.File;


/**
 * 文件下载信息类
 * @author fengruyi
 *
 */
public class DownLoadTaskInfo {
    public DownLoadTaskInfo() {
    }
    @Transient//不作为数据库字段
    private HttpHandler<File> handler;
    /**apk包名*/
    @Id
    private String packageName;
    /**文件下载状态*/
    private HttpHandler.State state;
    /**下载地址*/
    private String downloadUrl;
    /**文件名*/
    private String fileName;
    /**保存的文字路径*/
    private String fileSavePath;
    /**当前进度*/
    private long progress;
    /**文件长度，字节*/
    private long fileLength;
    /**版本*/
    private String versionName;
    /**版本号*/
    private int versionCode;
    /**应用图标地址*/
    private String iconUrl;
    /**下载速度*/
    /**自动恢复下载*/
    private boolean autoResume;
    @Transient//不作为数据库字段
    private int rate;
    
    /**文件大小，单位MB*/
    private float fileSizeOfMb;
    
    public float getFileSizeOfMb() {
		return fileSizeOfMb;
	}

	public void setFileSizeOfMb(float fileSizeOfMb) {
		this.fileSizeOfMb = fileSizeOfMb;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public HttpHandler<File> getHandler() {
        return handler;
    }

    public void setHandler(HttpHandler<File> handler) {
        this.handler = handler;
    }

    public HttpHandler.State getState() {
        return state;
    }

    public void setState(HttpHandler.State state) {
        this.state = state;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSavePath() {
        return fileSavePath;
    }

    public void setFileSavePath(String fileSavePath) {
        this.fileSavePath = fileSavePath;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }
    //包名相同则认为是同一个对象
    @Override
    public boolean equals(Object o) {
    	 if (this == o) return true;
         if (!(o instanceof DownLoadTaskInfo)) return false;

         DownLoadTaskInfo that = (DownLoadTaskInfo) o;

         if (!this.getPackageName().equals(that.getPackageName())) return false;

         return true;
    }
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public boolean isAutoResume() {
		return autoResume;
	}

	public void setAutoResume(boolean autoResume) {
		this.autoResume = autoResume;
	}

}
