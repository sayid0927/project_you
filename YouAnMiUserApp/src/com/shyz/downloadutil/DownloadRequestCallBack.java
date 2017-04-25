package com.shyz.downloadutil;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.lang.ref.WeakReference;

public class DownloadRequestCallBack extends RequestCallBack<File> {
	
    @SuppressWarnings("unchecked")
    private void refreshListItem() {
        if (userTag == null) return;
        WeakReference<ViewHolder> tag = (WeakReference<ViewHolder>) userTag;
        ViewHolder holder = tag.get();
        if (holder != null) {
        	holder.refresh();

        }
    }

    @Override
    public void onStart() {
        refreshListItem();
    }

    @Override
    public void onLoading(long total, long current, boolean isUploading) {
        refreshListItem();
    }

    @Override
    public void onSuccess(ResponseInfo<File> responseInfo) {
        refreshListItem();
    }

    @Override
    public void onFailure(HttpException error, String msg) {
        refreshListItem();
    }

    @Override
    public void onCancelled() {
        refreshListItem();
    }
}
