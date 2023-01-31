package com.tv9news.login.fragments

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.app.tv_nine.login.adapters.PreferenceCommonAdapter
import com.facebook.shimmer.ShimmerFrameLayout
import com.tv9news.R
import com.tv9news.databinding.FragmentChooseCategoryBinding
import com.tv9news.home.activities.MainActivity
import com.tv9news.login.activities.LoginActivity
import com.tv9news.login.viewmodels.PreferenceViewModel
import com.tv9news.models.masterHit.Category
import com.tv9news.utils.base.BaseFragment
import com.tv9news.utils.extensions.removeFragment

class ChooseCategoryFragment :
    BaseFragment<FragmentChooseCategoryBinding>(R.layout.fragment_choose_category) {
    lateinit var genericAdapter: PreferenceCommonAdapter<Category>
    private lateinit var activity: LoginActivity
    val viewmodel: PreferenceViewModel by activityViewModels<PreferenceViewModel>()

    override fun observeData() {
        viewmodel.categoryPreferenceList.observe(viewLifecycleOwner, Observer { preferenceList ->
            preferenceList?.let { preferenceList ->
                genericAdapter.addItems(preferenceList)
            }
        })
    }

    override fun initialize() {
        binding.chooseCatTv.text = viewmodel.metaData.value?.category_meta?.title
        binding.modifyTv.text = viewmodel.metaData.value?.category_meta?.rule
        genericAdapter = PreferenceCommonAdapter(R.layout.item_category) {}
        val layoutManager = FlexboxLayoutManager(context)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        /*val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS*/
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = genericAdapter
        handleClickEvent()
    }

    private fun handleClickEvent() {
        with(binding) {
            backButton.setOnClickListener {
                activity.removeFragment()
            }
            nextButton.setOnClickListener {
                var langlist = viewmodel.languagePreferenceList.value?.filter {
                    it.isSelected
                }
                var statelist = viewmodel.statePreferenceList.value?.filter {
                    it.isSelected
                }
                var categoryList = viewmodel.categoryPreferenceList.value?.filter {
                    it.isSelected
                }
                var states: String = ""
                statelist?.forEach {
                    states = states + it.state_name
                }
                var category: String = ""
                categoryList?.forEach {
                    category = category + it.category_title
                }
                /*Toast.makeText(
                    context,
                    "Language:-> " + langlist?.get(0)?.lang_name,
                    Toast.LENGTH_SHORT
                ).show()
                Toast.makeText(context, "State:-> " + states, Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Category:-> " + category, Toast.LENGTH_SHORT).show()*/


                navigateToHomeActivity()
            }
            skipButton.setOnClickListener {
                navigateToHomeActivity()
            }
        }
    }

    fun navigateToHomeActivity(
    ){
        viewmodel.savePreferenceToRoom{
            startActivity(Intent(activity, MainActivity::class.java))
            activity.finish()
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as LoginActivity
    }


}