package com.bignerdranch.android.photogallery;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxnik on 12/1/17.
 */

public class FlickrFetchr {

    private static final String TAG = "FlickrFetchr";
    private static final String API_KEY = BuildConfig.FLICKR_API_KEY;
    private static final String FETCH_RECENT_METHOD = "flickr.photos.getRecent";
    private static final String SEARCH_METHOD = "flickr.photos.search";
    private static final Uri ENDPOINT = Uri.parse("https://api.flickr.com/services/rest/")
                                        .buildUpon()
                                        .appendQueryParameter("api_key", API_KEY)
                                        .appendQueryParameter("format", "json")
                                        .appendQueryParameter("nojsoncallback", "1")
                                        .appendQueryParameter("extras", "url_s")
                                        .build();

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            // will not actually connect to the endpoint until a getInputStream() call

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();

        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<GalleryItem> fetchRecentPhotos() {
        String url = buildUrl(FETCH_RECENT_METHOD, null);

        return downloadGalleryItems(url);
    }

    public List<GalleryItem> searchPhotos(String query) {
        String url = buildUrl(SEARCH_METHOD, query);

        return downloadGalleryItems(url);
    }

    private String buildUrl(String method, String query) {
        Uri.Builder uriBuilder = ENDPOINT.buildUpon().appendQueryParameter("method", method);

        if (method.equals(SEARCH_METHOD)) {
            uriBuilder.appendQueryParameter("text", query);
        }

        return uriBuilder.build().toString();
    }

    private List<GalleryItem> downloadGalleryItems(String url) {

        List<GalleryItem> items = new ArrayList<>();

        try {
            String jsonString = getUrlString(url);

            Gson gson = new Gson();

            FlickrPhotos flickrPhotos = gson.fromJson(jsonString, FlickrPhotos.class);

            items = flickrPhotos.photos.photo;

        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }

        List<GalleryItem> filteredList = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            GalleryItem item = items.get(i);
            if (item.getUrl() != null) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }
}
