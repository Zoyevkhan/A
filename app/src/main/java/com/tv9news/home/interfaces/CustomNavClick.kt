package com.tv9news.home.activities.interfaces

import android.view.View
import android.widget.LinearLayout

interface CustomNavClick {
    fun getCategoris(cat: String)
    fun getBottomClick(cat: String, fab: View)
}