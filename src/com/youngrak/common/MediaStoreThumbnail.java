package com.youngrak.common;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.widget.ImageView;

public class MediaStoreThumbnail {
	
	private static final String TAG = "MediaStoreThumbnail";
	
	public void getThumbnail(int id, ImageView imageView, Context context){
		
		resetPurgeTimer();
        Bitmap bitmap = getBitmapFromCache(id);
        
        if (bitmap == null) {
        	forceThumbnail(id, imageView, context);
        } else {
        	imageView.setImageBitmap(bitmap);
        }
        
        
	}
	
	private void forceThumbnail(int id, ImageView imageView, Context context){
		if (id < 1) {
            imageView.setImageDrawable(null);
            return;
        }
		
		BitmapThumbnamilTask task = new BitmapThumbnamilTask(imageView);
        DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
        imageView.setImageDrawable(downloadedDrawable);
        imageView.setMinimumHeight(156);
        task.execute(id, context);
	}
	
	/**
     * @param imageView Any imageView
     * @return Retrieve the currently active download task (if any) associated with this imageView.
     * null if there is no such task.
     */
    private static BitmapThumbnamilTask getBitmapDownloaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadedDrawable) {
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
                return downloadedDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }
	
	/**
     * The actual AsyncTask that will asynchronously download the image.
     */
    class BitmapThumbnamilTask extends AsyncTask<Object, Void, Bitmap> {
        private int id;
        private Context context;
        private final WeakReference<ImageView> imageViewReference;

        public BitmapThumbnamilTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        /**
         * Actual download method.
         */
        @Override
        protected Bitmap doInBackground(Object... params) {
            id = (Integer)params[0];
            context = (Context)params[1];
            
            ContentResolver crThumb = context.getContentResolver();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
            return MediaStore.Video.Thumbnails.getThumbnail(crThumb, id, MediaStore.Video.Thumbnails.MICRO_KIND, options);
        }

        /**
         * Once the image is downloaded, associates it to the imageView
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            addBitmapToCache(id, bitmap);

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                BitmapThumbnamilTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
                // Change bitmap only if this process is still associated with it
                // Or if we don't use any bitmap to task association (NO_DOWNLOADED_DRAWABLE mode)
                if (this == bitmapDownloaderTask) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
    
    /**
     * A fake Drawable that will be attached to the imageView while the download is in progress.
     *
     * <p>Contains a reference to the actual download task, so that a download task can be stopped
     * if a new binding is required, and makes sure that only the last started download process can
     * bind its result, independently of the download finish order.</p>
     */
    static class DownloadedDrawable extends ColorDrawable {
        private final WeakReference<BitmapThumbnamilTask> bitmapDownloaderTaskReference;

        public DownloadedDrawable(BitmapThumbnamilTask bitmapDownloaderTask) {
            super(Color.BLACK);
            bitmapDownloaderTaskReference =
                new WeakReference<BitmapThumbnamilTask>(bitmapDownloaderTask);
        }

        public BitmapThumbnamilTask getBitmapDownloaderTask() {
            return bitmapDownloaderTaskReference.get();
        }
    }
	
	/*
     * Cache-related fields and methods.
     * 
     * We use a hard and a soft cache. A soft reference cache is too aggressively cleared by the
     * Garbage Collector.
     */
    
    private static final int HARD_CACHE_CAPACITY = 10;
    private static final int DELAY_BEFORE_PURGE = 10 * 1000; // in milliseconds

    // Hard cache, with a fixed maximum capacity and a life duration
    private final HashMap<Integer, Bitmap> sHardBitmapCache = 
    		new LinkedHashMap<Integer, Bitmap>(HARD_CACHE_CAPACITY / 2, 0.75f, true) {
		@Override
        protected boolean removeEldestEntry(LinkedHashMap.Entry<Integer, Bitmap> eldest) {
            if (size() > HARD_CACHE_CAPACITY) {
                // Entries push-out of hard reference cache are transferred to soft reference cache
                sSoftBitmapCache.put(eldest.getKey(), new SoftReference<Bitmap>(eldest.getValue()));
                return true;
            } else
                return false;
        }
    };
	
    // Soft cache for bitmaps kicked out of hard cache
    private final static ConcurrentHashMap<Integer, SoftReference<Bitmap>> sSoftBitmapCache =
        new ConcurrentHashMap<Integer, SoftReference<Bitmap>>(HARD_CACHE_CAPACITY / 2);
    
    /**
     * Adds this bitmap to the cache.
     * @param bitmap The newly downloaded bitmap.
     */
    private void addBitmapToCache(int id, Bitmap bitmap) {
        if (bitmap != null) {
            synchronized (sHardBitmapCache) {
                sHardBitmapCache.put(id, bitmap);
            }
        }
    }

    /**
     * @param url The URL of the image that will be retrieved from the cache.
     * @return The cached bitmap or null if it was not found.
     */
    private Bitmap getBitmapFromCache(int id) {
        // First try the hard reference cache
        synchronized (sHardBitmapCache) {
            final Bitmap bitmap = sHardBitmapCache.get(id);
            if (bitmap != null) {
                // Bitmap found in hard cache
                // Move element to first position, so that it is removed last
                sHardBitmapCache.remove(id);
                sHardBitmapCache.put(id, bitmap);
                return bitmap;
            }
        }

        // Then try the soft reference cache
        SoftReference<Bitmap> bitmapReference = sSoftBitmapCache.get(id);
        if (bitmapReference != null) {
            final Bitmap bitmap = bitmapReference.get();
            if (bitmap != null) {
                // Bitmap found in soft cache
                return bitmap;
            } else {
                // Soft reference has been Garbage Collected
                sSoftBitmapCache.remove(id);
            }
        }

        return null;
    }
    
	private final Handler purgeHandler = new Handler();

    private final Runnable purger = new Runnable() {
        public void run() {
            clearCache();
        }
    };
    
    /**
     * Clears the image cache used internally to improve performance. Note that for memory
     * efficiency reasons, the cache will automatically be cleared after a certain inactivity delay.
     */
    public void clearCache() {
        sHardBitmapCache.clear();
        sSoftBitmapCache.clear();
    }

	
	 /**
     * Allow a new delay before the automatic cache clear is done.
     */
    private void resetPurgeTimer() {
        purgeHandler.removeCallbacks(purger);
        purgeHandler.postDelayed(purger, DELAY_BEFORE_PURGE);
    }
}
