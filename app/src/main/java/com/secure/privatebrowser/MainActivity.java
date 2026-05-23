package com.secure.privatebrowser;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {
    private WebView myWebView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final int CAMERA_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Қолданба ашыла сала камераға рұқсат сұрау
        checkCameraPermission();

        // UI құрылымы және SwipeRefresh
        swipeRefreshLayout = new SwipeRefreshLayout(this);
        myWebView = new WebView(this);
        swipeRefreshLayout.addView(myWebView);
        setContentView(swipeRefreshLayout);

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myWebView.reload();
            }
        });

        myWebView.loadUrl("http://localhost:8080/");
    }

    // Камера рұқсатын тексеру және сұрау функциясы
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
                != PackageManager.PERMISSION_GRANTED) {
            
            // Егер рұқсат әлі берілмеген болса, жүйелік терезені шығару
            ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }
}
