package com.tv9news.login.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.tv9news.R
import com.tv9news.databinding.ActivityLoginBinding
import com.tv9news.login.fragments.ChooseLanguageFragment
import com.tv9news.login.viewmodels.PreferenceViewModel
import com.tv9news.models.user.User
import com.tv9news.utils.extensions.addFragment
import com.tv9news.utils.helpers.SharedPreference

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    val viewmodel:PreferenceViewModel by viewModels<PreferenceViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.mainContainer.setOnClickListener {

        }
        if (savedInstanceState == null) {
            if (SharedPreference.instance?.loggedInUser == null) {
                SharedPreference.instance?.loggedInUser = User("0", "Guest")
            }
            viewmodel.callPreferenceApi()
            addFragment(
                ChooseLanguageFragment(),
                binding.mainContainer.id
            )
        }
    }
}