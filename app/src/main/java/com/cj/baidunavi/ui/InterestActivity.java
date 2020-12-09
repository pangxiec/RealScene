package com.cj.baidunavi.ui;

import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.cj.baidunavi.R;

public class InterestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        WebView myWebView = (WebView) findViewById(R.id.id_webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        myWebView.loadUrl("https://720yun.com/t/9f6jzpuatn9?scene_id=8301489");
//        myWebView.loadUrl("http://47.107.109.237/interest.html");
    }

}
