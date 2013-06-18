package com.youngrak.common;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;


public class ImageUtil {
	
	
	public void getImageLoad(ImageView imageView, String url) {
		ImageLoadTask task = new ImageLoadTask(imageView);
		DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
		imageView.setImageDrawable(downloadedDrawable);
		task.execute(url);
	}
	
	/**
     * @param imageView Any imageView
     * @return Retrieve the currently active download task (if any) associated with this imageView.
     * null if there is no such task.
     */
    private static ImageLoadTask getBitmapDownloaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadedDrawable) {
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
                return downloadedDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }
    
    private class ImageLoadTask extends AsyncTask<Object, Void, Bitmap> {
	 	
    	private int id;
    	private final WeakReference<ImageView> imageViewReference;
    	
    	public ImageLoadTask(ImageView imageView){
    		imageViewReference = new WeakReference<ImageView>(imageView);
    	}
    	
 		protected void onPreExecute(){
 			super.onPreExecute();
 		}
 		
 		@Override
 		protected Bitmap doInBackground(Object... args) {
 			URL url;
 			Bitmap bmp = null;
			try {
				
				url = new URL(Constant.SERVER_ADDR + (String)args[0]);
				//id = (Integer)args[1];
				Options opts = new Options(); 
			    //opts.inJustDecodeBounds = true; 
			    opts.inSampleSize = 8;
			    /*
			    BitmapFactory.decodeFile(path, opts); 
			    Log.e("optwidth",opts.outWidth+""); 
			    opts.inJustDecodeBounds = false; 
			    if(opts.outWidth>500){ 
			            opts.inSampleSize = 4; 
			            bmp = BitmapFactory.decodeFile(path, opts); 
			    } 
			    else bmp = BitmapFactory.decodeFile(path, opts);
			     
				return bmp;
				
				
				//Decode image size
		        BitmapFactory.Options o = new BitmapFactory.Options();
		        o.inJustDecodeBounds = true;
		        BitmapFactory.decodeStream(new FileInputStream(f),null,o);
		
		        //The new size we want to scale to
		        final int REQUIRED_SIZE=70;
		
		        //Find the correct scale value. It should be the power of 2.
		        int scale=1;
		        while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
		            scale*=2;
		
		        //Decode with inSampleSize
		        BitmapFactory.Options o2 = new BitmapFactory.Options();
		        o2.inSampleSize=scale;
		        return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
				*/
			    
				
				return BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, opts);
				
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
 			return bmp;
 		}
 		
 		protected void onPostExecute(Bitmap bitmap) {
 			//addBitmapToCache(id, bitmap);

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                ImageLoadTask imageLoadTask = getBitmapDownloaderTask(imageView);
                
                if(this == imageLoadTask){
                	imageView.setImageBitmap(bitmap);
                }
            }
 		}
 		
 		@Override
 		protected void onCancelled() {
 			// TODO Auto-generated method stub
 			super.onCancelled();
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
        private final WeakReference<ImageLoadTask> bitmapDownloaderTaskReference;

        public DownloadedDrawable(ImageLoadTask bitmapDownloaderTask) {
            super(Color.BLACK);
            bitmapDownloaderTaskReference =
                new WeakReference<ImageLoadTask>(bitmapDownloaderTask);
        }

        public ImageLoadTask getBitmapDownloaderTask() {
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
