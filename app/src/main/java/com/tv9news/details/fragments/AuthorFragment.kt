package com.tv9news.details.fragments

import android.os.Bundle
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
import com.tv9news.details.adapters.AuthorAdapter
import com.tv9news.details.interfaces.AuthorClick
import com.tv9news.details.viewmodels.AuthorListViewModel
import com.tv9news.models.utils.EncryptionData
import com.tv9news.utils.helpers.AES
import com.tv9news.utils.helpers.Helper
import kotlinx.coroutines.launch

class AuthorFragment : Fragment(), AuthorClick{

    private lateinit var recyclerView: RecyclerView
    val viewmodel: AuthorListViewModel by activityViewModels<AuthorListViewModel>()
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_author, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        intiViews(view)
        observeData()
        val encryptionData = EncryptionData(lang_id = "1")
        val encryptionDataStr = Gson().toJson(encryptionData)
        val encryptionStr: String = AES.encrypt(encryptionDataStr)
        viewmodel.callDetailsApi(encryptionStr)
        observeEvents()
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            viewmodel.baseEvents.collect { UIEvent ->
                Helper.lifecycleScopeHandler(
                    requireContext(),
                    UIEvent,
                    shimmerFrameLayout,
                    recyclerView,
                    true
                )
            }
        }
    }

    private fun observeData() {
        viewmodel.detailsDataList.observe(viewLifecycleOwner, Observer { detailsDataList ->
            detailsDataList?.let { detailsDataList ->
                if (detailsDataList.isNotEmpty()) {
                    recyclerView.adapter = AuthorAdapter(detailsDataList, context!!, this)
                }
            }
        })
    }


    private fun intiViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.setHasFixedSize(true)
    }

    override fun AuthorClick(str: String) {
        (activity as DetailsActivity).openAuthorFragment(AuthorDetailsFragment().newInstance(str))
    }

}