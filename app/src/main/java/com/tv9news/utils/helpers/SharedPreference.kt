package com.tv9news.utils.helpers

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.tv9news.models.user.User

class SharedPreference private constructor() {
    private val sharedPreference: SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        sharedPreference = TV9Base.appContext!!.getSharedPreferences(MY_PREFERENCES, MODE)
        editor = sharedPreference.edit()
    }

    fun getString(key: String?): String {
        return sharedPreference.getString(key, "") ?: ""
    }

    fun putString(key: String?, value: String?) {
        editor.putString(key, value).commit()
    }

    fun getInt(key: String?): Int {
        return sharedPreference.getInt(key, 1)
    }

    fun putInt(key: String?, value: Int) {
        editor.putInt(key, value).commit()
    }

    fun getLong(key: String?): Long {
        return sharedPreference.getLong(key, 0L)
    }

    fun putLong(key: String?, value: Long) {
        editor.putLong(key, value).commit()
    }

    fun getFloat(key: String?): Float {
        return sharedPreference.getFloat(key, 0.5f)
    }

    fun putFloat(key: String?, value: Float) {
        editor.putFloat(key, value).commit()
    }

    fun getBoolean(key: String?): Boolean {
        return sharedPreference.getBoolean(key, false)
    }

    fun putBoolean(key: String?, value: Boolean) {
        editor.putBoolean(key, value).commit()
    }


    // remove after some time
    var loggedInUser: User?
        get() {
            var user: User? = null
            val userJson: String? = sharedPreference.getString(Constants.USER_LOGGED_IN, null)
            if (userJson != null && userJson.trim { it <= ' ' }.length > 0) {
                user = Gson().fromJson<User>(userJson, User::class.java)
            }
            return user
        }
        set(user) {
            editor.putString(Constants.USER_LOGGED_IN, Gson().toJson(user))
            editor.commit()
        }

    operator fun contains(key: String?): Boolean {
        return sharedPreference.contains(key)
    }

    fun remove(key: String?) {
        editor.remove(key).commit()
    }

    fun getString(s: String?, name: String?): String {
        return sharedPreference.getString(s, name) ?: ""
    }

    companion object {
        const val MY_PREFERENCES = "MY_PREFERENCES"
        const val MODE = Context.MODE_PRIVATE
        private var pref: SharedPreference? = null

        @JvmStatic
        val instance: SharedPreference?
            get() {
                if (pref == null) {
                    pref = SharedPreference()
                }
                return pref
            }
    }
}