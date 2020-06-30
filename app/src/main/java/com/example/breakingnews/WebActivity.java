package com.example.breakingnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        WebView webView = findViewById(R.id.webView);

        //Javascript enabled
        webView.getSettings().setJavaScriptEnabled(true);

        //by default, web will open up on the available browsers, i.e. Chrome.
        //the code below will tell the system that the webView will be launched within the app!
        webView.setWebViewClient(new WebViewClient());

        Intent intent = getIntent();

        String url = intent.getStringExtra("url");

        //Launch my website
        webView.loadUrl(url);
    }
}