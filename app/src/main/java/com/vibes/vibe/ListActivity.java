package com.vibes.vibe;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
                        updateVideosFound();
                    }
                });
            }
        }.start();
    }


}
