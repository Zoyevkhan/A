package com.tv9news.home.interfaces

import com.tv9news.models.home.Lists
import com.tv9news.models.home.SubCategory

interface HomeItemClick {
    fun itemHomeSubCatClick(data: SubCategory)
    fun itemHomeClick(data: Lists)
}