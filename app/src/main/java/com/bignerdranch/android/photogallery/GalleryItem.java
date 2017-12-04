package com.bignerdranch.android.photogallery;

/**
 * Created by maxnik on 12/1/17.
 */

public class GalleryItem {
    private String title;
    private String id;
    private String url_s;

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
}
