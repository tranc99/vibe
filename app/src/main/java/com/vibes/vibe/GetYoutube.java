package com.vibes.vibe;

import android.content.Context;
import android.util.Log;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ten on 4/29/15.
 */
public class GetYoutube {
    private YouTube youTube;
    private YouTube.Search.List query;

    public static final String KEY = "AIzaSyCi1AvuAlNgTIs5kLg12_i4bPF21-cwnZM";

    public GetYoutube(Context context) {
        youTube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) throws IOException {

            }
        }).setApplicationName(context.getString(R.string.app_name)).build();
        try {
            query = youTube.search().list("id,snippet");
            query.setKey(KEY);
            query.setType("video");
            query.setFields("items(id/videoId,snippet/title,snippet/description,snippet/thumbnails/default/url)");
        } catch (IOException e) {
            Log.d("YC", "Could not initialize: " +e);
        }
    }

    public List<VideoItem> search(String keywords) {
        query.setQ(keywords);
        try {
            SearchListResponse response = query.execute();
            List<SearchResult> results = response.getItems();

            List<VideoItem> items = new ArrayList<VideoItem>();
            for(SearchResult result:results) {
                VideoItem item = new VideoItem();
                item.title = result.getSnippet().getTitle();
                item.description = result.getSnippet().getDescription();
                item.thumbnailUrl = result.getSnippet().getThumbnails().getDefault().getUrl();
                item.id = result.getId().getVideoId();
                items.add(item);
            }
            return items;
        } catch (IOException e) {
            Log.d("YC", "Could not search: " +e);
            return null;
        }
    }

}
