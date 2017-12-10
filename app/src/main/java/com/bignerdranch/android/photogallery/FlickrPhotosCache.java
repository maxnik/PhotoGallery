package com.bignerdranch.android.photogallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;

import java.io.IOException;

/**
 * Created by maxnik on 12/10/17.
 */

public class FlickrPhotosCache extends LruCache<String, Bitmap> {
    private String TAG = "FlickrPhotosCache";

    public FlickrPhotosCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        int kbOfBitmap = value.getByteCount() / 1024;
        return kbOfBitmap;
    }

    @Override
    protected Bitmap create(String key) {
        Bitmap bitmap = null;

        try {

            byte[] bitmapBytes = new FlickrFetchr().getUrlBytes(key);
            bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0,
                    bitmapBytes.length);

        } catch (IOException ioe) {
            Log.e(TAG, "Error downloading image", ioe);
        }

        return bitmap;
    }
}
