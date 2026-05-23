package com.secure.privatebrowser;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myWebView = new WebView(this);
        setContentView(myWebView);

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        // Қарапайым WebViewClient: ешқандай бағыттаусыз, сілтемелерді ішінде ашады
        myWebView.setWebViewClient(new WebViewClient());

        // Браузер ашыла сала тікелей localhost серверіңізді жүктейді
        myWebView.loadUrl("http://localhost:8080/");
    }
}
