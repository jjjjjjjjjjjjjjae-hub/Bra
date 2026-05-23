package com.secure.privatebrowser;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {
    private WebView myWebView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Динамикалық түрде SwipeRefreshLayout құру
        swipeRefreshLayout = new SwipeRefreshLayout(this);
        myWebView = new WebView(this);
        
        // WebView-ді жаңарту контейнерінің ішіне орналастыру
        swipeRefreshLayout.addView(myWebView);
        setContentView(swipeRefreshLayout);

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        
        // Кэшті әрдайым серверден жаңартып алу баптауы (Оффлайн тұрып қалмауы үшін)
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // Бет толық жүктеліп біткенде айналып тұрған анимацияны тоқтату
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // Жоғарыдан төмен қарай тартқанда (Swipe) орындалатын әрекет:
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Кэшті ескермей, сайтты серверден қайта жаңадан жүктеу
                myWebView.reload();
            }
        });

        myWebView.loadUrl("http://localhost:8080/");
    }
}
