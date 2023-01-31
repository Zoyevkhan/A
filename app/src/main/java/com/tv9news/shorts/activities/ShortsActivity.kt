package com.tv9news.shorts.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.tv9news.R
import com.tv9news.home.interfaces.HomeItemClick
import com.tv9news.models.utils.EncryptionData
import com.tv9news.shorts.adapters.PagerAdapter
import com.tv9news.shorts.interfaces.ShortsItemClick
import com.tv9news.shorts.viewmodels.ShortsViewModel
import com.tv9news.utils.helpers.AES

class ShortsActivity : AppCompatActivity(), ShortsItemClick {

    private lateinit var viewPager: ViewPager
    private lateinit var closeImg: ImageView
    val viewmodel: ShortsViewModel by viewModels<ShortsViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shorts)
        initViews()
    }

    private fun initViews() {
        viewPager = findViewById(R.id.view_pager)
        closeImg = findViewById(R.id.closeImg)
         viewPager.adapter = PagerAdapter(this, this)
        val encryptionData = EncryptionData(lang_id = "1", article_type = "5", page = "1", content_type = intent.getStringExtra("type"))
        val encryptionDataStr = Gson().toJson(encryptionData)
        val encryptionStr: String = AES.encrypt(encryptionDataStr)
        viewmodel.callShortsApi(encryptionStr)

        closeImg.setOnClickListener {
            onBackPressed()
        }
    }

    override fun shortsClick(data: String) {
        startActivity(Intent(this, VideoShortActivity::class.java))
    }

}