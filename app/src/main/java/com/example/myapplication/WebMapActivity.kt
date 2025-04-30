package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class WebMapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_map)

        // Ascunde toolbar-ul doar pentru această activitate
        supportActionBar?.hide()

        val webView: WebView = findViewById(R.id.webView)

        // Permite JavaScript în WebView
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        // WebViewClient pentru a deschide linkurile în cadrul aplicației
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                return false
            }
        }

        // Încarcă fișierul map.html din assets
        webView.loadUrl("file:///android_asset/map.html")
    }

    override fun onResume() {
        super.onResume()
        // Asigură-te că atunci când revii pe WebMapActivity, toolbar-ul este ascuns
        supportActionBar?.hide()
    }

    override fun onPause() {
        super.onPause()
        // Ascunde toolbar-ul și când pleci de pe WebMapActivity
        supportActionBar?.hide()
    }
}
