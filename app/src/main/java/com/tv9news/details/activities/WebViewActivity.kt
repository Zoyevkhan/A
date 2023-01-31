package com.tv9news.details.activities

import android.annotation.TargetApi
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.tv9news.R
import com.tv9news.databinding.ActivityWebViewBinding
import com.tv9news.models.masterHit.Menu

class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding
    var url: String? = null
    val context: Context = this@WebViewActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_view)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setSupportActionBar(binding.toolbar)

        url = intent.getStringExtra("url")
        setWebData(url)

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun setWebData(url: String?) {
        // Configure related browser settings
        binding.webView.getSettings().setLoadsImagesAutomatically(true)
        binding.webView.getSettings().setJavaScriptEnabled(true)
        binding.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true)
        binding.webView.clearCache(true)
        binding.webView.clearHistory()
        binding.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY)
        binding.webView.getSettings()
            .setUseWideViewPort(true) // Zoom out if the content width is greater than the width of the viewport
        binding.webView.getSettings().setLoadWithOverviewMode(true)
        binding.webView.getSettings().setSupportZoom(true)
        binding.webView.getSettings().setBuiltInZoomControls(true) // allow pinch to zooom
        binding.webView.getSettings()
            .setDisplayZoomControls(false) // disable the default zoom controls on the page
        binding.webView.setBackgroundColor(Color.BLACK);
        binding.webView.setWebViewClient(MyBrowser())
        binding.webView.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                binding.progressBar.setVisibility(View.VISIBLE)
                if (newProgress == 100) {
                    binding.progressBar.setVisibility(View.GONE)
                }
                super.onProgressChanged(view, newProgress)
            }
        })
        binding.webView.loadUrl(url.toString())
    }

    private inner class MyBrowser : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.endsWith(".pdf") || url.endsWith(".PDF")) {
                val path = Uri.parse(url)
                val pdfIntent = Intent(Intent.ACTION_VIEW)
                pdfIntent.setDataAndType(path, "application/pdf")
                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                try {
                    context.startActivity(pdfIntent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(
                        context,
                        "No Application available to view PDF",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                view.loadUrl(url)
            }
            binding.progressBar.setVisibility(View.VISIBLE)
            return true
        }

        @TargetApi(Build.VERSION_CODES.N)
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            if (request.getUrl().toString().endsWith(".pdf") || request.getUrl().toString()
                    .endsWith(".PDF")
            ) {
                val path = Uri.parse(request.getUrl().toString())
                val pdfIntent = Intent(Intent.ACTION_VIEW)
                pdfIntent.setDataAndType(path, "application/pdf")
                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                try {
                    startActivity(pdfIntent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(
                        this@WebViewActivity,
                        "No Application available to view PDF",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                view.loadUrl(request.getUrl().toString())
            }
            binding.progressBar.setVisibility(View.VISIBLE)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }
    }

    fun reloadWebView() {
        if (binding.webView != null) {
            binding.webView.reload()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFinishAfterTransition()
        return true
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            // Let the system handle the back button
            super.onBackPressed()
        }
    }
}