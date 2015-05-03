package com.vibes.vibe;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class ListActivity extends Activity {

    private WebView engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        engine = (WebView) findViewById(R.id.web_engine);
        engine.setWebViewClient(new MyWebViewClient());

        engine.getSettings().setJavaScriptEnabled(true);

        engine.loadUrl("http://youtube.com/");
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
