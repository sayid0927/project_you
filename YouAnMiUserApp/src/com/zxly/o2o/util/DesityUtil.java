package com.zxly.o2o.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

/**
 * px dp 转换
 * 
 * @author wuchenhui
 * 
 */
public class DesityUtil {

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @param scale
     *            （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @param scale
     *            （DisplayMetrics类中属性density）
     * @return
     */
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param fontScale
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param fontScale
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale);
    }

	@SuppressWarnings("deprecation")
	public static int[] getScreenSizes(Context context){
		 WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		    Display display = manager.getDefaultDisplay();
			int width =display.getWidth();
		    int height=display.getHeight();
		    return new int[]{width,height};
	}
	
	/**
	 * TODO 打印图片在手机显示实际高度
	 * 
	 * @param d
	 * @param context
	 * @return
	 */
	public static int[] getDrawableSize(Drawable d,Context context){
		
		Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
		Log.d("o2o_user", "with"+bitmap.getWidth()+ "  height="+bitmap.getHeight() );
		Log.d("o2o_user", "DPwith"+px2dp(context, bitmap.getWidth())+ " DP height="+px2dp(context, bitmap.getHeight()));
		return null;
	}

}
