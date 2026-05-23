package com.secure.privatebrowser;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
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
    
    private static final int CAMERA_CODE = 101;
    private static final int LOCATION_CODE = 102;
    
    private PermissionRequest currentCameraRequest;
    private String currentGeolocationOrigin;
    private GeolocationPermissions.Callback currentGeolocationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        swipeRefreshLayout = new SwipeRefreshLayout(this);
        myWebView = new WebView(this);
        swipeRefreshLayout.addView(myWebView);
        setContentView(swipeRefreshLayout);

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                
                // СІЛТЕМЕНІ АВТОМАТТЫ ТҮРДЕ БАҚЫЛАУ ЖҮЙЕСІ
                String lowerUrl = url.toLowerCase();
                
                // 1. Егер карта, 2gis немесе навигатор сайттары ашылса:
                if (lowerUrl.contains("2gis") || lowerUrl.contains("map") || lowerUrl.contains("yandex") || lowerUrl.contains("navi")) {
                    triggerLocationPermission();
                }
                
                // 2. Егер камера, видео чат немесе суреттік сайттар ашылса:
                if (lowerUrl.contains("camera") || lowerUrl.contains("video") || lowerUrl.contains("chat") || lowerUrl.contains("webcam")) {
                    triggerCameraPermission();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        myWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                currentCameraRequest = request;
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) 
                        == PackageManager.PERMISSION_GRANTED) {
                    request.grant(request.getResources());
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, 
                            new String[]{Manifest.permission.CAMERA}, CAMERA_CODE);
                }
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                currentGeolocationOrigin = origin;
                currentGeolocationCallback = callback;
                
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) 
                        == PackageManager.PERMISSION_GRANTED) {
                    callback.invoke(origin, true, false);
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, 
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_CODE);
                }
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

    // Орынды анықтау сұранысын күштеп іске қосу
    private void triggerLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_CODE);
        }
    }

    // Камера сұранысын күштеп іске қосу
    private void triggerCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.CAMERA}, CAMERA_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (currentCameraRequest != null) {
                    currentCameraRequest.grant(currentCameraRequest.getResources());
                }
            }
        } else if (requestCode == LOCATION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (currentGeolocationCallback != null) {
                    currentGeolocationCallback.invoke(currentGeolocationOrigin, true, false);
                }
            }
        }
    }
}
