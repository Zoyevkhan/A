package com.tv9news.home.activities.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.gson.Gson
import com.tv9news.R
import com.tv9news.details.activities.DetailsActivity
import com.tv9news.details.activities.WebViewActivity
import com.tv9news.home.activities.adapters.MainNewsAdapter
import com.tv9news.home.activities.viewmodels.HomeViewModel
import com.tv9news.home.adapters.TopSubcateogryImageAdapter
import com.tv9news.home.adapters.TopSubcateogyItemAdapter
import com.tv9news.home.interfaces.HomeItemClick
import com.tv9news.models.home.Lists
import com.tv9news.models.masterHit.Menus
import com.tv9news.models.masterHit.SubCategory
import com.tv9news.models.utils.EncryptionData
import com.tv9news.utils.helpers.AES
import com.tv9news.utils.helpers.Constants
import com.tv9news.utils.helpers.Helper
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), HomeItemClick {

    private lateinit var mainRecyclerView: RecyclerView
    private lateinit var topRecyclerView: RecyclerView
    val viewmodel: HomeViewModel by activityViewModels<HomeViewModel>()
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private var menuId: String = ""
    private var menuName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        intiViews(view)
    }

    private fun intiViews(view: View) {
        mainRecyclerView = view.findViewById(R.id.mainRecyclerView)
        topRecyclerView = view.findViewById(R.id.topRecyclerView)
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout)
        mainRecyclerView.setHasFixedSize(false)
    }

    private fun setTopRecyclerView(subCategory: List<SubCategory>) {
        if (subCategory[0].app_view_type == "0"){
            topRecyclerView.adapter = TopSubcateogyItemAdapter(subCategory, context!!)
            topRecyclerView.isNestedScrollingEnabled = false
            topRecyclerView.setHasFixedSize(true)
            topRecyclerView.visibility = View.VISIBLE
        }else{
            topRecyclerView.adapter = TopSubcateogryImageAdapter(subCategory, context!!)
            topRecyclerView.isNestedScrollingEnabled = false
            topRecyclerView.setHasFixedSize(true)
            topRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun observeData() {
        viewmodel.homeDataList.observe(viewLifecycleOwner, Observer { homeList ->
            homeList?.let { preferenceList ->
                if (preferenceList.isNotEmpty()) {
                    mainRecyclerView.adapter = MainNewsAdapter(preferenceList, context!!, this)
                    viewmodel._homeDataList.value = ArrayList()
                }
            }
        })
    }

    fun newInstance(menu: Menus): HomeFragment {
        val fragmentHome = HomeFragment()
        val args = Bundle()
        args.putSerializable("menu", menu)
        fragmentHome.arguments = args
        return fragmentHome
    }

    private fun observeEvents(){
        lifecycleScope.launch {
            viewmodel.baseEvents.collect { UIEvent ->
                Helper.lifecycleScopeHandler(
                    requireContext(),
                    UIEvent,
                    shimmerFrameLayout,
                    mainRecyclerView,
                    true
                )
            }
        }
    }

    override fun onResume() {
//        if (!shimmerFrameLayout.isVisible) mainRecyclerView.visibility =
//            View.VISIBLE
        super.onResume()
        observeData()
        observeEvents()

        val menus: Menus = arguments?.getSerializable("menu") as Menus
        Log.d("menuss", menus.toString())

        if (menus.menu_action_element == "1") {
            val encryptionData = EncryptionData(lang_id = "1", state_id = "0", category_id = "0")
            val encryptionDataStr = Gson().toJson(encryptionData)
            val encryptionStr: String = AES.encrypt(encryptionDataStr)
            viewmodel.callHomeApi(encryptionStr, "home")
        } else {
            val subCatId = if (menus.sub_category.isNotEmpty()) {
                menus.sub_category.get(0).id
            } else {
                menus.id
            }
            val encryptionData =
                EncryptionData(lang_id = "1", state_id = "0", category_id = subCatId)
            val encryptionDataStr = Gson().toJson(encryptionData)
            val encryptionStr: String = AES.encrypt(encryptionDataStr)
            viewmodel.callHomeApi(encryptionStr, "other")
            if (menus.sub_category.isNotEmpty()) {
                setTopRecyclerView(menus.sub_category)
            }
        }
    }

    override fun itemHomeSubCatClick(data: com.tv9news.models.home.SubCategory) {
        TODO("Not yet implemented")
    }

    override fun itemHomeClick(data: Lists) {
        data.article_type?.let { Helper.showToast(requireActivity(), it) }
        when (data.article_type) {
            Constants.ARTICLE_DETAIL -> {
                openDetailScreen(data)
            }
            Constants.PHOTO_DETAIL -> {
                openDetailScreen(data)
            }
            Constants.VIDEO_DETAIL -> {
                openDetailScreen(data)
            }
            Constants.PODCAST_DETAIL -> {
                openDetailScreen(data)
            }
            Constants.SHORTS_DETAIL -> {

            }
            Constants.LIVE_BLOG_DETAIL -> {

            }
            Constants.WEBSTORY_DETAIL -> {
                val intent =
                    Intent(context, WebViewActivity::class.java).putExtra("url", data.media_url)
                startActivity(intent)
            }
            else -> {

            }
        }
    }

    fun openDetailScreen(data: Lists) {
        val intent = Intent(context, DetailsActivity::class.java)
            .putExtra("articleId", data.id)
            .putExtra("articleType", data.article_type)
        startActivity(intent)
    }
}