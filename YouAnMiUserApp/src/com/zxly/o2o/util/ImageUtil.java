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
import com.zxly.o2o.config.Config;
import com.zxly.o2o.controller.AppController;

import java.io.InputStream;

/**
 *     @author dsnx  @version 创建时间：2015-1-12 下午1:51:05    类说明: 
 */
public class ImageUtil {

	public static BitmapFactory.Options opts_rgb565;
	public static BitmapFactory.Options opts_argb4444;
	public static BitmapFactory.Options opts_rgb565_generate;
	public static BitmapFactory.Options opts_argb4444_generate;

	static public Options getOptimizedOptions(boolean generatedRes) {

		return generatedRes ? opts_argb4444_generate : opts_argb4444;

	}

	static {
		opts_rgb565 = new BitmapFactory.Options();
		opts_rgb565.inDensity = DisplayMetrics.DENSITY_HIGH;
		opts_rgb565.inTargetDensity = Config.densityDpi;
		opts_rgb565.inPreferredConfig = Bitmap.Config.RGB_565;
		opts_rgb565.inPurgeable = true;
		opts_rgb565.inInputShareable = true;

		opts_argb4444 = new BitmapFactory.Options();
		opts_argb4444.inDensity = DisplayMetrics.DENSITY_HIGH;
		opts_argb4444.inTargetDensity = Config.densityDpi;
		opts_argb4444.inPreferredConfig = Bitmap.Config.ARGB_4444;
		opts_argb4444.inPurgeable = true;
		opts_argb4444.inInputShareable = true;

		opts_rgb565_generate = new BitmapFactory.Options();
		opts_rgb565_generate.inDensity = Config.densityDpi;
		opts_rgb565_generate.inTargetDensity = Config.densityDpi;
		opts_rgb565_generate.inPreferredConfig = Bitmap.Config.RGB_565;
		opts_rgb565_generate.inPurgeable = true;
		opts_rgb565_generate.inInputShareable = true;

		opts_argb4444_generate = new BitmapFactory.Options();
		opts_argb4444_generate.inDensity = Config.densityDpi;
		opts_argb4444_generate.inTargetDensity = Config.densityDpi;
		opts_argb4444_generate.inPreferredConfig = Bitmap.Config.ARGB_4444;
		opts_argb4444_generate.inPurgeable = true;
		opts_argb4444_generate.inInputShareable = true;

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
		Bitmap bmp ;
		InputStream is = ctx.getResources().openRawResource(resId);
		bmp = BitmapFactory.decodeStream(is, null, ImageUtil.getOptimizedOptions(false));
		return bmp;
	}

	public static void setImage(NetworkImageView imageView, String url, int defaultDrawable, View
			view) {
		//		imageView.setErrorImageResId(defaultDrawable);
		imageView.setImageUrl(null, null);
		imageView.setDefaultImageResId(defaultDrawable);
		if (url != null && !"".equals(url)) {
			imageView.setVisibility(View.VISIBLE);
			imageView.setImageUrl(url, AppController.imageLoader);
		} else if (view==null) {
			imageView.setVisibility(View.VISIBLE);
		} else {
			view.setVisibility(View.GONE);
		}
	}
}
