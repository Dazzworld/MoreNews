package com.dazzcoder.morenews;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.http.RequestManager;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;

public class AppContext extends Application {

    private static ImageLoader sImageLoader = null;
    public static Context context;
    private final NetworkImageCache imageCacheMap = new NetworkImageCache();


    public static ImageLoader getImageLoader() {
        return sImageLoader;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        context = this;
        RequestManager.getInstance().init(this);
        sImageLoader = new ImageLoader(RequestManager.getInstance().getRequestQueue(), imageCacheMap);
    }

    public static synchronized Context getAppContext() {
        return context;
    }


    @SuppressLint("NewApi")
    public static class NetworkImageCache extends LruCache<String, Bitmap> implements ImageCache {

        public NetworkImageCache() {
            this(getDefaultLruCacheSize());
        }

        public NetworkImageCache(int sizeInKiloBytes) {
            super(sizeInKiloBytes);
        }

        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight() / 1024;
        }

        @Override
        public Bitmap getBitmap(String url) {
            return get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            put(url, bitmap);
        }

        public static int getDefaultLruCacheSize() {
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 6;
            return cacheSize;
        }
    }


}
