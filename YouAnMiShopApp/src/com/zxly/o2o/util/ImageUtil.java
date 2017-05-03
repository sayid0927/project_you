package com.zxly.o2o.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;

import com.android.volley.toolbox.NetworkImageView;
import com.zxly.o2o.application.AppController;

import java.io.InputStream;

/**
 *     @author dsnx  @version 创建时间：2015-1-12 下午1:51:05    类说明: 
 */
public class ImageUtil {

	public static Options opts_rgb565;
	public static Options opts_argb4444;
	public static Options opts_rgb565_generate;
	public static Options opts_argb4444_generate;

	static public Options getOptimizedOptions(boolean generatedRes) {

		return generatedRes ? opts_argb4444_generate : opts_argb4444;

	}

	public static Drawable zoomBitmap(Bitmap bitmap, int w) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float scaleWH = ((float) width / height);
		int h = (int) (w / scaleWH);
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) w / width);
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidth, scaleHeight);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		return new BitmapDrawable(AppController.getInstance().getResources(),
				bitmap);
	}

	public static Bitmap LoadResourceBitmap(Context ctx, int resId) {
		Bitmap bmp = null;
		InputStream is = ctx.getResources().openRawResource(resId);
		bmp = BitmapFactory.decodeStream(is, null, ImageUtil.getOptimizedOptions(false));
		return bmp;
	}

	public static void setImage(NetworkImageView imageView, String url, int defaultDrawable, boolean isShowDefaul) {
		//		imageView.setErrorImageResId(defaultDrawable);
		imageView.setDefaultImageResId(defaultDrawable);
		if (url != null && !"".equals(url)) {
			imageView.setVisibility(View.VISIBLE);
			imageView.setImageUrl(url, AppController.imageLoader);
		} else if (isShowDefaul) {
			imageView.setVisibility(View.VISIBLE);
		} else {
			imageView.setVisibility(View.GONE);
		}
	}
}
