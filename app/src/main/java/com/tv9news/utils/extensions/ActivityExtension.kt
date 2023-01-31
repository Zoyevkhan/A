package com.tv9news.utils.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.addFragment(fragment: Fragment, containerId: Int) {
    supportFragmentManager.beginTransaction().add(containerId, fragment).commit()
}

fun AppCompatActivity.replaceFragment(
    fragment: Fragment,
    containerId: Int,
    shouldBeAddToBackStack: Boolean
) {
    var transaction = supportFragmentManager.beginTransaction().replace(
        containerId,
        fragment
    )
    transaction = if (shouldBeAddToBackStack) transaction.addToBackStack("xx") else transaction
    transaction.commit()
}

fun AppCompatActivity.removeFragment() {
    supportFragmentManager.popBackStack()
}