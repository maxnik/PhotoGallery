package com.bignerdranch.android.photogallery;

import android.net.Uri;

/**
 * Created by maxnik on 12/1/17.
 */

public class GalleryItem {
    private String title;
    private String id;
    private String url_s;
    private String owner;

    @Override
    public String toString() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url_s;
    }

    public Uri getPhotoPageUri() {
        return Uri.parse("https://www.flickr.com/photos/")
                .buildUpon()
                .appendPath(owner)
                .appendPath(id)
                .build();
    }
}
