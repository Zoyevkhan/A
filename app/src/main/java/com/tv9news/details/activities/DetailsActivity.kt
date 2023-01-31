package com.tv9news.details.activities

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.tv9news.R
import com.tv9news.databinding.ActivityDetailsBinding
import com.tv9news.details.viewmodels.DetailsViewModel
import com.tv9news.home.activities.fragments.ArticleDetailsFragment
import com.tv9news.home.activities.fragments.PodcastDetailsFragment
import com.tv9news.home.activities.fragments.VideoDetailsFragment
import com.tv9news.utils.helpers.Constants

class DetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailsBinding
    val viewmodel: DetailsViewModel by viewModels<DetailsViewModel>()
    var articleId: String? = null
    var articleType: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details)
        setSupportActionBar(binding.toolbar)

        articleId = intent.getStringExtra("articleId")
        articleType = intent.getStringExtra("articleType")

        if (savedInstanceState == null) {
            if (articleType.equals(Constants.VIDEO_DETAIL)) {
                replaceFragment(VideoDetailsFragment().newInstance(articleId))
            }else if (articleType.equals(Constants.PODCAST_DETAIL)) {
                replaceFragment(PodcastDetailsFragment().newInstance(articleId))
            }else {
                //replaceFragment(ArticleDetailsFragment().newInstance(articleId))
                replaceFragment(PodcastDetailsFragment().newInstance(articleId))
            }
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fm = supportFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.replace(R.id.detailsContainer, fragment)
        ft.commit()
    }

    fun openAuthorFragment(fragment: Fragment){
        binding.relativeTop.visibility = View.GONE
        binding.title.text = getString(R.string.author)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.detailsContainer, fragment)
        transaction.addToBackStack("author")
        transaction.commit()
    }

}