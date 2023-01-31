package com.app.tv_nine.login.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tv9news.R
import com.tv9news.databinding.ItemCategoryBinding
import com.tv9news.databinding.ItemLanguageBinding
import com.tv9news.databinding.ItemStateBinding
import com.tv9news.models.masterHit.Category
import com.tv9news.models.masterHit.Language
import com.tv9news.models.masterHit.State
import com.tv9news.utils.helpers.Helper.loadImage

class PreferenceCommonAdapter<T : Any>(
    @LayoutRes val layoutId: Int,
    val onPreferenceClick: (Int) -> Unit
) : RecyclerView.Adapter<PreferenceCommonAdapter.GenericViewHolder>() {

    private val containerList = mutableListOf<T>()
    fun addItems(items: List<T>) {
        this.containerList.clear()
        this.containerList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GenericViewHolder(
            DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(parent.context),
                layoutId,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = containerList.size

    override fun onBindViewHolder(holder: GenericViewHolder, position: Int) {
        var itemContainer = containerList[position]
        when (holder.binding) {
            is ItemLanguageBinding -> {
                handleLanguageUI(holder.binding, itemContainer as Language, position)
            }
            is ItemStateBinding -> {
                handleStateUI(holder.binding, itemContainer as State, position)
            }
            is ItemCategoryBinding -> {
                handleCategoryUI(holder.binding, itemContainer as Category, position)
            }
        }
    }

    fun handleLanguageUI(binding: ItemLanguageBinding, language: Language, position: Int) {
        with(binding) {
            title.text = language.lang_name
            loadImage(logo, language.lang_logo, language.logo_sizes)
            selectionLogo.setImageResource(if (language.isSelected) R.drawable.option_select else R.drawable.option_unselect)
            container.setOnClickListener {
                language.isSelected = true
                containerList.forEachIndexed { index, item ->
                    if ((item as Language).isSelected && index != position) {
                        (item as Language).isSelected = false
                        notifyItemChanged(index)
                    }
                }
                notifyItemChanged(position)
                onPreferenceClick.invoke(position)
            }
        }
    }

    fun handleStateUI(binding: ItemStateBinding, state: State, position: Int) {
        with(binding) {
            title.text = state.state_name
            loadImage(logo, state.state_logo, state.logo_sizes)
            container.setBackgroundResource(if (!state.isSelected) R.drawable.state_shape_unselect else R.drawable.state_shape_select)
            selectImage.setImageResource(if (!state.isSelected) R.drawable.option_add else R.drawable.option_select)
            container.setOnClickListener {
                state.isSelected = !state.isSelected
                notifyItemChanged(position)
            }
        }
    }

    fun handleCategoryUI(binding: ItemCategoryBinding, category: Category, position: Int) {
        with(binding) {
            title.isSelected = true
            title.text = category.category_title
            loadImage(logo, category.category_logo, category.logo_sizes)
            container.setBackgroundResource(if (!category.isSelected) R.drawable.item_shape_unselect else R.drawable.item_shape_select)
            selectionLogo.setImageResource(if (!category.isSelected) R.drawable.option_add else R.drawable.option_add_selected)
            container.setOnClickListener {
                category.isSelected = !category.isSelected
                notifyItemChanged(position)
            }
        }
    }

    class GenericViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)


}