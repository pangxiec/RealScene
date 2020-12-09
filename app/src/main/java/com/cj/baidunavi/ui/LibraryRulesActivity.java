package com.cj.baidunavi.ui;

import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.cj.baidunavi.R;

public class LibraryRulesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_rules);
    }
    @Override
    protected void onStart()
    {
        super.onStart();

        WebView myWebView = findViewById(R.id.id_webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        myWebView.loadUrl("http://47.107.109.237/LRules.html");
    }
}
