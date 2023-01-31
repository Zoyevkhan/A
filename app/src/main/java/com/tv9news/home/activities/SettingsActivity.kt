package com.tv9news.home.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.tv9news.R

class SettingsActivity : AppCompatActivity() {

    private lateinit var backImg: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initViews()
    }

    private fun initViews() {
        backImg = findViewById(R.id.backImg)
        backImg.setOnClickListener { finish() }
    }
}