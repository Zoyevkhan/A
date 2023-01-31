package com.tv9news.login.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.app.tv_nine.login.adapters.PreferenceCommonAdapter
import com.tv9news.R
import com.tv9news.databinding.FragmentChooseStateBinding
import com.tv9news.login.activities.LoginActivity
import com.tv9news.login.viewmodels.PreferenceViewModel
import com.tv9news.models.masterHit.State
import com.tv9news.utils.base.BaseFragment
import com.tv9news.utils.extensions.removeFragment
import com.tv9news.utils.extensions.replaceFragment
import kotlinx.coroutines.launch

class ChooseStateFragment :  BaseFragment<FragmentChooseStateBinding>(R.layout.fragment_choose_state) {
    lateinit var genericAdapter:PreferenceCommonAdapter<State>
    private lateinit var activity :LoginActivity
    val viewmodel: PreferenceViewModel by activityViewModels<PreferenceViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun observeData() {
        viewmodel.statePreferenceList.observe(viewLifecycleOwner, Observer { _preferenceList ->
            _preferenceList?.let {
                    preferenceList->
                genericAdapter.addItems(preferenceList)
            }
        })
    }

    override fun initialize(){
        genericAdapter = PreferenceCommonAdapter(R.layout.item_state) {

        }
        binding.chooseLanguageTv.text = viewmodel.metaData.value?.state_meta?.title
        binding.modifyTv.text = viewmodel.metaData.value?.state_meta?.rule
        binding.recyclerView.adapter = genericAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        handleClickEvent()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity =context as LoginActivity
    }

    fun handleClickEvent(){
        with(binding){
            backButton.setOnClickListener {
                activity.removeFragment()
            }
            skipButton.setOnClickListener {
                moveToNextFragment()
            }
            nextButton.setOnClickListener {
                moveToNextFragment()
            }
        }
    }
    fun moveToNextFragment(
    ){
        activity.replaceFragment(
            ChooseCategoryFragment(),
            (requireView().parent as ViewGroup).id,
            true
        )
    }
}