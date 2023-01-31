package com.tv9news.login.fragments

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.tv_nine.login.adapters.PreferenceCommonAdapter
import com.tv9news.R
import com.tv9news.databinding.FragmentChooseLanguageBinding
import com.tv9news.login.activities.LoginActivity
import com.tv9news.login.viewmodels.PreferenceViewModel
import com.tv9news.models.masterHit.Language
import com.tv9news.room.LocalRoomDatabase
import com.tv9news.utils.base.BaseFragment
import com.tv9news.utils.extensions.replaceFragment
import com.tv9news.utils.helpers.Helper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ChooseLanguageFragment :BaseFragment<FragmentChooseLanguageBinding>(R.layout.fragment_choose_language) {
    lateinit var activity:LoginActivity
    lateinit var genericAdapter:PreferenceCommonAdapter<Language>
    private var selectedLanguageList:MutableList<Language> = mutableListOf()
    val viewmodel:PreferenceViewModel by activityViewModels<PreferenceViewModel>()

    override fun observeData() {
        viewmodel.languagePreferenceList.observe(viewLifecycleOwner, Observer { preferenceList ->
            preferenceList?.let {
                    preferenceList->
                genericAdapter.addItems(preferenceList)
            }
        })
    }

    override fun observeEvents() {
        lifecycleScope.launch {
            viewmodel.baseEvents.collect { UIEvent ->
                Helper.lifecycleScopeHandler(
                    requireContext(),
                    UIEvent,
                    binding.shimmerLayout.shimmerFrameLayout,
                    binding.container,
                    true
                )
            }
        }
    }

    override fun initialize() {
        genericAdapter = PreferenceCommonAdapter(R.layout.item_language){
            viewmodel.updateCategoryAndState(it)
            moveToNext()
        }
        selectedLanguageList = mutableListOf()
        binding.recylerview.adapter=genericAdapter
        binding.recylerview.layoutManager=LinearLayoutManager(requireContext())

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as LoginActivity
    }

    fun moveToNext() {
        activity.replaceFragment(
            ChooseStateFragment(),
            (requireView().parent as ViewGroup).id, true
        )
    }

    override fun onResume() {
        if (!binding.shimmerLayout.shimmerFrameLayout.isVisible) binding.container.visibility =
            View.VISIBLE
        super.onResume()
    }
}