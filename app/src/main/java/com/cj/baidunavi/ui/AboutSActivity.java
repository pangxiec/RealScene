package com.cj.baidunavi.ui;

import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.cj.baidunavi.R;

public class AboutSActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_s);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        WebView myWebView = (WebView) findViewById(R.id.id_webView);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // 加载首页 (不支持HTTPS)
        myWebView.loadUrl("http://www.cqust.edu.cn/"); //mobile
        // myWebView.loadUrl("http://192.168.1.109:8080/demo/webapp/index.jsp");
    }

}
