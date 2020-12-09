package com.cj.baidunavi.ui;

import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.cj.baidunavi.R;

public class LappointmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lappointment);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        WebView myWebView = findViewById(R.id.id_webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        myWebView.loadUrl("http://lib.cqust.edu.cn:8000/default.aspx");
    }
}
