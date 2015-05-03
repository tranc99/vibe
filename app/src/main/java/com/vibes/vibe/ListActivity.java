package com.vibes.vibe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class ListActivity extends Activity {

    private EditText searchInput;
    private ListView videosFound;

    private Handler handler;
    private List<VideoItem> searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        searchInput = (EditText) findViewById(R.id.search_text);
        videosFound = (ListView) findViewById(R.id.videos);

        handler = new Handler();

        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    searchOnYoutube(v.getText().toString());
                    return false;
                }
                return true;
            }
        });
    }


    private void searchOnYoutube(final String keywords) {
        new Thread() {
            public void run() {
                GetYoutube butler = new GetYoutube(ListActivity.this);
                searchResults = butler.search(keywords);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        updateVideos();
                    }
                });
            }
        }.start();
    }

    private void updateVideos() {
        ArrayAdapter<VideoItem> adapter = new ArrayAdapter<VideoItem>(getApplicationContext(), R.layout.each_video, searchResults) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.each_video, parent, false);
                }
                ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
                TextView title = (TextView) convertView.findViewById(R.id.video_title);
                TextView description = (TextView) convertView.findViewById(R.id.description);

                VideoItem searchResult = searchResults.get(position);

                Picasso.with(getApplicationContext()).load(searchResult.thumbnailUrl).into(thumbnail);
                title.setText(searchResult.title);
                description.setText(searchResult.description);
                return convertView;
            }
        };

        videosFound.setAdapter(adapter);
    }

    private void addClickListener() {
        videosFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
                intent.putExtra("VIDEO_ID", searchResults.get(position).id);
                startActivity(intent);
            }
        });
    }

}
