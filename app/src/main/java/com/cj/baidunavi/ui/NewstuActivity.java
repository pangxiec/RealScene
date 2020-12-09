package com.cj.baidunavi.ui;

import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.cj.baidunavi.R;

public class NewstuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newstu);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        WebView myWebView = (WebView) findViewById(R.id.id_webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new MyWebViewClient());


        // 加载首页 (不支持HTTPS)
        myWebView.loadUrl("http://47.107.109.237/new.html");
    }

    private class MyWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
        {
            return false;
        }
    }
}
