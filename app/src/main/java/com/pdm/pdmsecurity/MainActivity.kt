package com.pdm.pdmsecurity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import com.pdm.pdmsecurity.databinding.ActivityMainBinding
import com.pdm.testwebview.MyWebView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var webClient: MyWebView

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        if (applicationContext.checkSelfPermission(
                "android.permission.POST_NOTIFICATIONS"
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf("android.permission.POST_NOTIFICATIONS"),
                101
            );
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val wev = findViewById<WebView>(R.id.web_content)
        webClient = MyWebView()
        wev.webViewClient = webClient
        wev.settings.javaScriptEnabled = true;
        wev.settings.domStorageEnabled = true;
        wev.setWebChromeClient(WebChromeClient())
        wev.webViewClient = object : WebViewClient() {
            @SuppressLint("NewApi")
            override fun shouldInterceptRequest(
                view: WebView,
                request: WebResourceRequest
            ): WebResourceResponse? {
                try {
                    val cookies: String =
                        CookieManager.getInstance().getCookie(request.url.toString())
                    readCookies(cookies)
                    Log.d("", "request.getRequestHeaders()::" + request.requestHeaders)
                } catch (ex: java.lang.Exception) {

                }
                return null
            }
        }
        wev.webChromeClient = WebChromeClient()
        wev.settings.databaseEnabled = true
        wev.settings.allowFileAccess = true
        wev.settings.allowContentAccess = true
        wev.settings.cacheMode = WebSettings.LOAD_DEFAULT
        wev.loadUrl("http://109.122.199.199:4200")
        startService(Intent(applicationContext, BackService::class.java))
    }

    private fun readCookies(cookies: String) {
        val cookieList = cookies.split(';')
        for (cookie in cookieList) {
            if (cookie.contains("userId")) {
                val temp1 = cookie.split("=".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                val cookieValue = temp1[1]
                if (cookieValue != null || cookieValue != "") {
                    try {
                        ValueHandler.UserId = cookieValue
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

}