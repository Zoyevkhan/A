package com.tv9news.home.activities.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.NonNull
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.gson.Gson
import com.tv9news.R
import com.tv9news.details.PodcastPlayerManagerListener
import com.tv9news.details.adapters.PodcastAudioAdapter
import com.tv9news.details.interfaces.OnPodcastClickListener

import com.tv9news.details.podcast.general.PodcastStatus
import com.tv9news.details.podcast.general.errors.OnInvalidPathListener
import com.tv9news.details.podcast.view.PodcastPlayerView
import com.tv9news.details.viewmodels.DetailsViewModel
import com.tv9news.models.home.Lists
import com.tv9news.models.utils.EncryptionData
import com.tv9news.utils.helpers.AES
import com.tv9news.utils.helpers.Constants
import com.tv9news.utils.helpers.Helper
import kotlinx.coroutines.launch


class PodcastDetailsFragment : Fragment(), OnInvalidPathListener, PodcastPlayerManagerListener,
    OnPodcastClickListener {
    private lateinit var container: RelativeLayout
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    var articleId = ""
    val viewmodel: DetailsViewModel by activityViewModels<DetailsViewModel>()
    lateinit var player: PodcastPlayerView
    lateinit var podcastRecyclerView: RecyclerView
    var podcastAudioAdapter: PodcastAudioAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_podcast_details, container, false)
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
        val encryptionData = EncryptionData(lang_id = "1", article_id = "21156")
        val encryptionDataStr = Gson().toJson(encryptionData)
        val encryptionStr: String = AES.encrypt(encryptionDataStr)
        viewmodel.callDetailsApi(encryptionStr)
    }

    private fun intiViews(view: View) {
        container = view.findViewById(R.id.container)
        podcastRecyclerView = view.findViewById(R.id.recyclerView)
        player = view.findViewById(R.id.podcastplayer)
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout)
        (podcastRecyclerView.getItemAnimator() as SimpleItemAnimator).setSupportsChangeAnimations(
            false
        )
    }

    private fun observeData() {
        viewmodel.detailsDataList.observe(viewLifecycleOwner, Observer { detailsDataList ->
            detailsDataList?.let { detailsDataList ->
                if (detailsDataList.isNotEmpty()) {
                    player.visibility = View.VISIBLE
                    podcastRecyclerView.visibility = View.VISIBLE


                    var detailsDataLists = detailsDataList
                    val jcAudios: ArrayList<Lists> = ArrayList<Lists>()
                    detailsDataList?.forEach { dataHome ->
                        if (dataHome.layout_code.equals(Constants.DETAIL_PODCAST_HEADER)) {
                            jcAudios.add(0,
                                Lists(
                                    id = dataHome.details.id,
                                    article_title = dataHome.details.article_title,
                                    article_type = dataHome.details.article_type,
                                    media_url = dataHome.details.media_url,
                                    duration = dataHome.details.duration,
                                    article_image = dataHome.details.article_image,
                                    posted_on = dataHome.details.posted_on,
                                    category_name = dataHome.details.category_name,
                                    tags = dataHome.details.tags
                                )
                            )
                        } else if (dataHome.layout_code.equals(Constants.DETAIL_MORE_LIST)) {
                            jcAudios.addAll(dataHome.list)
                        }
                    }
                    adapterSetup(jcAudios)
                }
            }
        })
    }

    fun newInstance(articleId: String?): PodcastDetailsFragment {
        val fragmentHome = PodcastDetailsFragment()
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

    //podcast init
    override fun onStop() {
        super.onStop()
        player.createNotification()
    }

    override fun onPause() {
        super.onPause()
        player.createNotification()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.kill()
    }

    override fun onItemClick(position: Int) {
        player.playAudio(player.myPlaylist.get(position))
    }

    fun adapterSetup(jcAudios: List<Lists>) {
        player.initPlaylist(jcAudios, this)
        podcastRecyclerView.adapter = PodcastAudioAdapter(player.myPlaylist, this)
    }

    fun updateProgress(jcStatus: PodcastStatus) {
        requireActivity().runOnUiThread(Runnable {
            var progress =
                ((jcStatus.getDuration().toFloat() - jcStatus.getCurrentPosition().toFloat())
                        / jcStatus.getDuration().toFloat())
            progress = 1.0f - progress
            if (podcastAudioAdapter != null) {
                podcastAudioAdapter!!.updateProgress(jcStatus.getJcAudio(), progress)
            }
        })

    }

    override fun onPathError(jcAudio: Lists) {}
    override fun onPreparedAudio(status: PodcastStatus) {}
    override fun onCompletedAudio() {}
    override fun onPaused(status: PodcastStatus) {}
    override fun onContinueAudio(status: PodcastStatus) {}
    override fun onPlaying(status: PodcastStatus) {}
    override fun onStopped(status: PodcastStatus) {}
    override fun onJcpError(@NonNull throwable: Throwable) {}
    override fun onTimeChanged(@NonNull status: PodcastStatus) {
        updateProgress(status)
    }
}