package com.tv9news.home.activities.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.util.Rational
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.gson.Gson
import com.jwplayer.pub.api.JWPlayer
import com.jwplayer.pub.api.configuration.PlayerConfig
import com.jwplayer.pub.api.events.EventType
import com.jwplayer.pub.api.events.FullscreenEvent
import com.jwplayer.pub.api.events.listeners.VideoPlayerEvents
import com.jwplayer.pub.view.JWPlayerView
import com.tv9news.R
import com.tv9news.details.activities.DetailsActivity
import com.tv9news.details.fragments.AuthorFragment
import com.tv9news.details.interfaces.DetailsItemClick
import com.tv9news.details.jwplayer.KeepScreenOnHandler
import com.tv9news.details.jwplayer.PlayerEventHandler
import com.tv9news.details.viewmodels.DetailsViewModel
import com.tv9news.home.activities.adapters.DetailNewsAdapter
import com.tv9news.models.utils.EncryptionData
import com.tv9news.utils.helpers.AES
import com.tv9news.utils.helpers.Constants
import com.tv9news.utils.helpers.Helper
import kotlinx.coroutines.launch


class VideoDetailsFragment : Fragment(), DetailsItemClick,
    VideoPlayerEvents.OnFullscreenListener {
    private lateinit var container: LinearLayout
    private lateinit var detailRecyclerView: RecyclerView
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private lateinit var jwplayer: JWPlayerView
    var articleId = ""
    var mPlayer: JWPlayer? = null
    var rational: Rational? = null
    val viewmodel: DetailsViewModel by activityViewModels<DetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_video_details, container, false)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        articleId = arguments?.getString("articleId").toString()
        intiViews(view)
        observeData()
        observeEvents()
        callAPI(articleId)
    }

    private fun callAPI(articleId: String) {
        if (detailRecyclerView != null && detailRecyclerView.adapter != null) {
            (detailRecyclerView.adapter as DetailNewsAdapter).stopTextAudio()
        }
        val encryptionData = EncryptionData(lang_id = "1", article_id = articleId)
        val encryptionDataStr = Gson().toJson(encryptionData)
        val encryptionStr: String = AES.encrypt(encryptionDataStr)
        viewmodel.callDetailsApi(encryptionStr)
    }

    private fun intiViews(view: View) {
        container = view.findViewById(R.id.container)
        detailRecyclerView = view.findViewById(R.id.detailRecyclerView)
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout)
        jwplayer = view.findViewById(R.id.jwplayer)
        detailRecyclerView.setHasFixedSize(false)
    }

    private fun observeData() {
        viewmodel.detailsDataList.observe(viewLifecycleOwner, Observer { detailsDataList ->
            detailsDataList?.let { detailsDataList ->
                if (detailsDataList.isNotEmpty()) {
                    detailRecyclerView.adapter = DetailNewsAdapter(detailsDataList, requireActivity(), this)
                    if (jwplayer != null) {
                        mPlayer = jwplayer.getPlayer()
                        initPlayer()
                    }
                }
            }
        })
    }

    fun newInstance(articleId: String?): VideoDetailsFragment {
        val fragmentHome = VideoDetailsFragment()
        val args = Bundle()
        args.putString("articleId", articleId)
        fragmentHome.arguments = args
        return fragmentHome
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            viewmodel.baseEvents.collect { UIEvent ->
                Helper.lifecycleScopeHandler(
                    requireContext(),
                    UIEvent,
                    shimmerFrameLayout,
                    container,
                    true
                )
            }
        }
    }

    override fun onResume() {
        if (!shimmerFrameLayout.isVisible) container.visibility = View.VISIBLE
        super.onResume()
    }

    override fun AuthorClick(str: String?) {
        (activity as DetailsActivity).openAuthorFragment(AuthorFragment())
    }

    override fun itemDetailClick(str: String?) {
        str?.let { callAPI(it) }
    }

    //jw player init
    fun initPlayer() {
        rational = Rational(jwplayer.getWidth(), jwplayer.getHeight())
        // Handle hiding/showing of ActionBar
        mPlayer!!.addListener(EventType.FULLSCREEN, this)
        // Keep the screen on during playback
        KeepScreenOnHandler(mPlayer!!, (context as Activity).window)
        PlayerEventHandler(mPlayer!!)

        val config = PlayerConfig.Builder()
            .playlistUrl("https://cdn.jwplayer.com/v2/playlists/Ia1O4zNP?format=json")
            .build()

        mPlayer!!.setup(config)

        jwplayer.getPlayerAsync(requireContext(), this) { player: JWPlayer ->
            mPlayer = player
            mPlayer!!.registerActivityForPip(
                requireActivity(),
                (activity as AppCompatActivity?)!!.supportActionBar
            )
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (mPlayer != null && !mPlayer!!.isInPictureInPictureMode()) {
            val isFullscreen = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
            mPlayer!!.setFullscreen(isFullscreen, true)
        }
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode)
        if (isInPictureInPictureMode) {
            val actionBar: android.app.ActionBar? = (requireActivity() as Activity).actionBar
            actionBar?.hide()
        }
    }

    override fun onFullscreen(fullscreenEvent: FullscreenEvent?) {
        val actionBar: android.app.ActionBar? = (requireActivity() as Activity).actionBar
        if (actionBar != null) {
            if (fullscreenEvent!!.getFullscreen()) {
                actionBar.hide()
            } else {
                actionBar.show()
            }
        }
    }
}