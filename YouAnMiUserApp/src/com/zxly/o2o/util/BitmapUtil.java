package com.zxly.o2o.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.zxly.o2o.activity.BasicMyCircleAct;
import com.zxly.o2o.controller.AppController;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 图片转换工具
 */
public class BitmapUtil {

	public Bitmap resizeAtleast(Bitmap src, int min) {
		if (src == null) {
			return null;
		}
		int width = src.getWidth();
		int height = src.getHeight();
		float rate = 0.0f;

		if (width < height) {
			rate = (float) min / width;
			height = (int) (height * rate);
			width = min;
		} else {
			rate = (float) min / height;
			width = (int) (width * rate);
			height = min;
		}
		return Bitmap.createScaledBitmap(src, width, height, true);
	}

	public Bitmap crop(Bitmap src, int w, int h) {
		int width = src.getWidth();
		int height = src.getHeight();
		int x = 0;
		int y = 0;
		if (width > w) {
			x = (width - w) / 2;
		}
		if (height > h) {
			y = (height - h) / 2;
		}
		int cw = w;
		int ch = h;
		if (w > width) {
			cw = width;
		}
		if (h > height) {
			ch = height;
		}
		return Bitmap.createBitmap(src, x, y, cw, ch);
	}

	/**
	 * 根据长宽压缩图片
	 * 
	 * @param src
	 * @param w
	 * @param h
	 * @return Bitmap
	 */
	public Bitmap resizeAndCrop(Bitmap src, int w, int h) {
		Bitmap resizedImage = resizeAtleast(src, w > h ? w : h);
		if (resizedImage != null) {
			Bitmap result = crop(src, w, h);
			recycle(resizedImage);
			return result;
		} else {
			return null;
		}
	}

	/**
	 * 放大1.25倍
	 * 
	 * @param bitmap
	 */
	public Bitmap zoomBitmap(Bitmap oldbmp) {
		int width = oldbmp.getWidth();
		int height = oldbmp.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = 1.25f;
		float scaleHeight = 1.25f;
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
		return newbmp;
	}

	/**
	 * 旋转图片
	 * 
	 * @param bitmap
	 */
	public Bitmap rotate(Bitmap src, float degrees, boolean recycleSrc) {
		Matrix matrix = new Matrix();
		matrix.postRotate(degrees);
		Bitmap rotatedBitmap = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(),
				matrix, true);
		if (recycleSrc) {
			recycle(src);
		}
		return rotatedBitmap;
	}

	/**
	 * 回收Bitmap图片对象
	 * 
	 * @param bitmap
	 */
	public void recycle(Bitmap bitmap) {
		if (bitmap != null) {
			if (!bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
				System.gc();
			}

		}
	}

	/**
	 * 计算快捷方式图标
	 * 
	 * @param bitmap
	 */
	public Bitmap toShortcut(Context ctx, int drawable) {

		int size = 50;
		float density = getDensity(ctx);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		// BitmapFactory.decodeFile(p + path + ".png", opts);
		BitmapFactory.decodeResource(ctx.getResources(), drawable, opts);
		// 计算图片缩放比例
		final int minSideLength = Math.min((int) (size * density), (int) (size * density));
		opts.inSampleSize = computeSampleSize(opts, minSideLength, (int) (size * density)
				* (int) (size * density));
		opts.inJustDecodeBounds = false;
		opts.inInputShareable = true;
		opts.inPurgeable = true;
		// Bitmap bm = BitmapFactory.decodeFile(p + path + ".png", opts);
		return BitmapFactory.decodeResource(ctx.getResources(), drawable, opts);

	}

	public float getDensity(Context ctx) {
		DisplayMetrics dm = new DisplayMetrics();
		Activity activity = (Activity) ctx;
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.density;
	}

	/**
	 * 直接从SD卡加载图片
	 * 
	 * @param context
	 * @param path
	 * @return
	 */
	public static Bitmap getSDImg(Context context, String imagePath) {

		Bitmap bitmap = null;
		InputStream is = null;
		try {
			File file = new File(imagePath);
			if (!file.exists()) {
				return null;
			}
			if (file.isDirectory()) {
				return null;
			}
			imagePath = "file://" + imagePath;
			AppLog.i("info", imagePath);

			is = context.getContentResolver().openInputStream(Uri.parse(imagePath));
			bitmap = BitmapFactory.decodeStream(is);

		} catch (FileNotFoundException e) {
			AppLog.p(e);
		} catch (OutOfMemoryError e) {
			AppLog.p(e);
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (Exception e) {
				AppLog.p(e);
			}
		}
		return bitmap;
	}

	public void setPhotoBitmap(Uri uri, int size) throws FileNotFoundException, IOException {
		InputStream input = AppController.getInstance().getContentResolver().openInputStream(uri);
		BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
		onlyBoundsOptions.inJustDecodeBounds = true;
		onlyBoundsOptions.inDither = true;// optional
		onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
		BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
		input.close();
		if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
			return;
		int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight
				: onlyBoundsOptions.outWidth;
		double ratio = (originalSize > size) ? (originalSize / size) : 1.0;
		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
		bitmapOptions.inDither = true;// optional
		bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
		input = AppController.getInstance().getContentResolver().openInputStream(uri);
		BasicMyCircleAct.photoBitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
		input.close();
	}

	private int getPowerOfTwoForSampleRatio(double ratio) {
		int k = Integer.highestOneBit((int) Math.floor(ratio));
		return (k == 0) ? 1 : k;
	}

	/**
	 * Options,指定像素加载SD卡图片
	 * 
	 * @param context
	 * @param path
	 * @return
	 */
	public Bitmap getSDImgByDensity(Context context, String path, int width, int height) {
		Bitmap bitmap = null;
		try {
			File mfile = new File(path);
			if (mfile.exists()) {
				BitmapFactory.Options opts = null;
				opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(path, opts);
				float density = context.getResources().getDisplayMetrics().density;
				final int minSideLength = Math.min((int) (width * density),
						(int) (height * density));
				opts.inSampleSize = computeSampleSize(opts, minSideLength, (int) (width * density)
						* (int) (height * density));
				opts.inJustDecodeBounds = false;
				opts.inInputShareable = true;
				opts.inPurgeable = true;
				bitmap = BitmapFactory.decodeFile(path, opts);
			}
		} catch (Exception e) {
			AppLog.p(e);
		}
		return bitmap;
	}

	public int computeSampleSize(BitmapFactory.Options options, int minSideLength,
			int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength,
			int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h
				/ maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	// /**开机动画*/
	// public static Drawable initLauchImage(Context context) {
	// String imageName = PreferUtil.getInstance().getLauchImageName();
	// String path = FileUtils.getImageDownloadDir(context);
	// FileUtils.createSDDir(path);
	// String imagePath = path + imageName;
	// File file = new File(path);
	// if (file.exists()) {
	// Bitmap bitmap =null;
	// if(!"".equals(imageName)){
	// bitmap = getSDImg(context, imagePath);
	// }
	// if(bitmap!=null){
	// Drawable mDrawable=new BitmapDrawable(bitmap);
	// //BitmapUtil.recycle(bitmap);
	// return mDrawable;
	// }
	// }
	// return null;
	// }
	/** 图片缩放 */
	public Bitmap zoomBitmap(Drawable drawable, int mWidth, int mVWidth, int mVHeight,
			float mDensity) {
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidht = ((float) mWidth / width);
		float scaleHeight = ((float) mVHeight / height);
		matrix.postScale(scaleWidht, scaleHeight);
		Bitmap scaledBitmap = null;
		try {
			scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		} catch (OutOfMemoryError e) {
		}
		return scaledBitmap;
	}

	/** 获取imageView图片 */
	public Bitmap loadBitmapFromView(View v, float density) {
		Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width, v.getLayoutParams().height,
				Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		v.layout((int) (8 * density), (int) (8 * density), (int) (64 * density),
				(int) (64 * density));
		v.draw(c);
		return b;
	}

	/**
	 * 加载本地图片 http://bbs.3gstdy.com
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public  Bitmap decodeUriAsBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(AppController.getInstance().getContentResolver()
					.openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	/**
	 * 从服务器取图片 http://bbs.3gstdy.com
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap getHttpBitmap(String url) {
		URL myFileUrl;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setConnectTimeout(0);
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}


	/**
	 * 图片缩放(主要是生成微信,微博分享缩略图)
	 * 主要是把图片尺寸缩小，放到32kb以下
	 *  */
	public Bitmap getWxShareIcon(Bitmap bitmap) {
		int maxLen=32*1000;

		if(bitmap==null||bitmap.isRecycled()|| bmpToByteArray(bitmap, false).length<maxLen)
			return bitmap;

		int iconSize=100;
		int minSize=40;
		int cutRate=25;

		float width = bitmap.getWidth();
		float height = bitmap.getHeight();

		float mWidth=0;
		float mHeight=0;

		float rate=width/height;

		Bitmap scaledBitmap = null;

		android.util.Log.d("shareImg"," no zip  mwith:"+mWidth +" mHeight :"+mHeight   +"   with="+width +"    height="+height);
		try {
			while (iconSize>minSize) {
				if(width>=height &&width>iconSize){
					mWidth=iconSize;
					mHeight= mWidth/rate;
				}else if(width<height&&height>iconSize){
					mHeight=iconSize;
					mWidth=mHeight*rate;
				}else{
					return  bitmap; //没办法压缩
				}

				Matrix matrix = new Matrix();
				float scaleWidht = mWidth / width;
				float scaleHeight = mHeight / height;
				matrix.postScale(scaleWidht, scaleHeight);

				scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) width, (int) height, matrix, true);

				if(bmpToByteArray(scaledBitmap, false).length>(maxLen-2000)){
					iconSize-=cutRate;
				}else{
					android.util.Log.d("shareImg", " 尺寸压缩xxx最后：  width="+mWidth +"  height="+ mHeight + "  size:"+ (bmpToByteArray(scaledBitmap, false).length / 1000) + "kb");
					break;
				}

			}

			//bitmap.recycle();
		} catch (OutOfMemoryError e) {
		}

		return scaledBitmap;

	}



	public File saveBitmapToFile(Bitmap bm,String fileName)  {
		OutputStream out=null;
		try {
			File dir=new File(Constants.STORE_IMG_PATH);
			if(!dir.exists())
				dir.mkdirs();

			String path = Constants.STORE_IMG_PATH+File.separator+fileName;
			File imageFile = new File(path);

			out=new FileOutputStream(imageFile);
			BufferedOutputStream bos = new BufferedOutputStream(out);
			bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			bos.close();

			if(imageFile.exists())
				return imageFile;
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			try {
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}



	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static Bitmap drawableToBitmap(Drawable drawable)
	{
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}


}
