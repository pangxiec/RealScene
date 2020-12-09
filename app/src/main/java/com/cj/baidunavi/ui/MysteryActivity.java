package com.cj.baidunavi.ui;

import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.cj.baidunavi.R;

/*
   神秘组织模块
 */
public class MysteryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mystery);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        WebView myWebView = (WebView) findViewById(R.id.id_webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new MyWebViewClient());

        myWebView.loadUrl("http://47.107.109.237/mystery.html");
    }

    private class MyWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
        {
            // 决定怎么样打开一个新的网址: 开一个新窗口 or 用本窗口打开 or 第三方打开 ..
            return false;
        }
    }
}
