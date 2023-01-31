package com.tv9news.utils.helpers

import android.content.Context
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.text.format.DateUtils
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tv9news.R
import org.jsoup.Jsoup
import java.io.InputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


object Helper {
    fun loadImage(imageView: ImageView, url: String, imageSize: String) {
        if (!TextUtils.isEmpty(url)) {
            Glide
                .with(imageView.context)
                .load(url)
                .fitCenter()
                .placeholder(R.color.app_background)
                .error(R.color.app_background)
                .into(imageView)
        }else{

        }
    }

    fun readJSONFromAsset(context: Context, s: String): String? {
        var json: String? = null
        try {
            val inputStream: InputStream = context.assets.open(s)
            json = inputStream.bufferedReader().use { it.readText() }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    fun showLogToast(context: Context, message: String, isShowToast: Boolean) {
        Log.d(context.packageName, "" + message)
        if (isShowToast) Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show()
    }

    fun lifecycleScopeHandler(
        context: Context,
        uiEvent: UIEvent,
        shimmerFrameLayout: ShimmerFrameLayout,
        container: View,
        showShimmer: Boolean
    ) {
        when (uiEvent) {
            is UIEvent.onLoading -> {
                isShowShimmer(container, shimmerFrameLayout, showShimmer)
            }
            is UIEvent.Success -> {
                isShowShimmer(container, shimmerFrameLayout, false)
                showLogToast(context, uiEvent.message, false)
            }
            is UIEvent.OnError -> {
                isShowShimmer(container, shimmerFrameLayout, false)
                showLogToast(context, uiEvent.message, true)
            }
            is UIEvent.Exception -> {
                isShowShimmer(container, shimmerFrameLayout, false)
                showLogToast(context, uiEvent.exception.message.toString(), true)
            }
            else -> {
                isShowShimmer(container, shimmerFrameLayout, false)
                showLogToast(context, context.getString(R.string.something_wrong), true)
            }
        }
    }

    fun isShowShimmer(container: View, shimmerFrameLayout: ShimmerFrameLayout, isShow: Boolean) {
        if (isShow) shimmerFrameLayout.startShimmerAnimation() else shimmerFrameLayout.stopShimmerAnimation()
        shimmerFrameLayout.visibility = if (isShow) View.VISIBLE else View.GONE
        container.visibility = if (isShow) View.GONE else View.VISIBLE
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun setNiceTimeMilis(ts: String): String {
        var edateString = ""
        if (!TextUtils.isEmpty(ts)) {
            edateString = DateUtils.getRelativeTimeSpanString(
                ts.toLong() * 1000,
                Calendar.getInstance().timeInMillis,
                DateUtils.MINUTE_IN_MILLIS
            ) as String
        } else {
            edateString = ""
        }
        return edateString
    }

    fun setDateMilis(ts: String): String {
        var edateString = ""
        if (!TextUtils.isEmpty(ts)) {
            val fe: DateFormat = SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.US)
            val end_millis: Long = ts.toLong() * 1000
            val de = Date(end_millis)
            edateString = fe.format(de)
        } else {
            edateString = ""
        }
        return edateString
    }

    private fun shortsBottomSheetDialog(context: Context, fab: ImageView) {
        val shortsBottomSheet = BottomSheetDialog(context, R.style.bottomsheetDialogTheme)
        shortsBottomSheet.setContentView(R.layout.bottom_sheet_home_shorts)
        Objects.requireNonNull(shortsBottomSheet.window)?.attributes!!.windowAnimations =
            R.style.PauseDialogAnimation
        shortsBottomSheet.setCancelable(false)
        shortsBottomSheet.setCanceledOnTouchOutside(false)
        val articleShorts = shortsBottomSheet.findViewById<LinearLayout>(R.id.articleShorts)
        val videoShorts = shortsBottomSheet.findViewById<LinearLayout>(R.id.videoShorts)
        val podcastShorts = shortsBottomSheet.findViewById<LinearLayout>(R.id.podcastShorts)

        articleShorts?.setOnClickListener {
            shortsBottomSheet.cancel()
            shortsBottomSheet.dismiss()
        }

        videoShorts?.setOnClickListener {
            shortsBottomSheet.cancel()
            shortsBottomSheet.dismiss()
        }

        podcastShorts?.setOnClickListener {
            shortsBottomSheet.cancel()
            shortsBottomSheet.dismiss()
        }

        if (!shortsBottomSheet.isShowing) {
            shortsBottomSheet.show()
        } else {
        }
    }

    fun getHtmlUpdatedData(value: String): String? {
        val base64version: String
        base64version = if (!TextUtils.isEmpty(value)) {
            Base64.encodeToString(value.trim { it <= ' ' }.toByteArray(), Base64.DEFAULT)
        } else {
            ""
        }
        return base64version
    }

    fun showWebData(context: Context, data: String?, mContentWebView: WebView, background: Int) {
        mContentWebView.setBackgroundColor(background)
        mContentWebView.setWebChromeClient(WebChromeClient())
        mContentWebView.getSettings().setPluginState(WebSettings.PluginState.ON)
        mContentWebView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND)
        mContentWebView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
        mContentWebView.setWebViewClient(MyWebViewClient())
        mContentWebView.getSettings().setJavaScriptEnabled(true)
        mContentWebView.loadDataWithBaseURL(
            getSrcUrlFromHtml(data), fromHtmlRegex(context, data!!)!!,
            "text/html; charset=utf-8", "UTF-8", null
        )
    }

    class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {
            return true
        }
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            return if (isVideoUrl(url)) {
                false
            } else true
        }
    }

    fun fromHtmlRegex(context: Context, html: String?): String? {
        if (html!!.contains("width=") && html!!.contains("height=")) {
            val displayMetrics: DisplayMetrics = context.resources.displayMetrics
            val width: Float = displayMetrics.widthPixels / displayMetrics.density - 16
            val height: Float = 220F
            var finalHtml =
                html!!.replace("width=\"([0-9]{1,4})\"".toRegex(), "width=\"" + width + "\"")
                    .replace("height=\"([0-9]{1,4})\"".toRegex(), "height=\"" + height + "\"")
            return finalHtml
        } else {
            return html
        }
    }


    fun getSrcUrlFromHtml(data: String?): String? {
        val pattern1 = Regex("(?<=src=\").*?(?=[\\?\"])")
        val ans: MatchResult? = pattern1.find(data!!, 0)
        return (ans?.value)
    }

    private fun isVideoUrl(url: String): Boolean {
        val splitedArray = url.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val lastValueOfArray = splitedArray[splitedArray.size - 1]
        if (isYoutubeUrl(url) || lastValueOfArray == "mp4" || lastValueOfArray == "flv" || lastValueOfArray == "m4a" || lastValueOfArray == "3gp" || lastValueOfArray == "mkv") {
            return true
        }
        return false
    }

    fun isYoutubeUrl(youTubeURl: String): Boolean {
        youTubeURl.trim { it <= ' ' }
        val parts: Array<String> = youTubeURl.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val regex1 =
            "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|watch\\?v%3D|\u200C\u200B%2Fvideos%2F|embed%2\u200C\u200BF|youtu.be%2F|%2Fv%2\u200C\u200BF)[^#\\&\\?\\n]*"
        val pattern1 = Pattern.compile(regex1, Pattern.MULTILINE)
        for (i in parts.indices) {
            val matcher1 = pattern1.matcher(parts[i])
            if (matcher1.find()) {
                return true
            }
        }
        return false
    }

    fun fromHtml(html: String?): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString()
        } else {
            Html.fromHtml(html).toString()
        }
    }

    fun htmlToText(html: String?): String? {
        return Jsoup.parse(html).text().toString()
    }
}