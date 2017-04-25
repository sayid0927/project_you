package com.zxly.o2o.cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * @author     dsnx
 * @version    YIBA-O2O 2014-12-24
 * @since      YIBA-O2O
 */
public class BitmapLruCache implements ImageCache
{
    
    private LruCache<String, Bitmap> mCache;
    
    private static BitmapLruCache lruImageCache;
    
    public static BitmapLruCache instance()
    {
        if (lruImageCache == null)
        {
            lruImageCache = new BitmapLruCache();
        }
        return lruImageCache;
    }
    
    public BitmapLruCache()
    {
        
        int maxMemory = (int)Runtime.getRuntime().maxMemory();
        int maxSize = maxMemory / 8;
        mCache = new LruCache<String, Bitmap>(maxSize)
        {
            @Override
            protected int sizeOf(String key, Bitmap bitmap)
            {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }
    
    @Override
    public Bitmap getBitmap(String url)
    {
        return mCache.get(url);
    }
    
    @Override
    public void putBitmap(String url, Bitmap bitmap)
    {
        mCache.put(url, bitmap);
    }
    
}
