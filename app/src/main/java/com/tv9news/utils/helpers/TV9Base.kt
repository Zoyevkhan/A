package com.tv9news.utils.helpers

import android.content.Context
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.preference.PreferenceManager
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.jwplayer.pub.api.license.LicenseUtil

class TV9Base : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
        singleton = this
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        PreferenceManager.getDefaultSharedPreferences(this)
        // TODO: Add your license key
        LicenseUtil().setLicenseKey(this, Constants.JWP_LICENSE_KEY)
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    protected override fun attachBaseContext(base: Context?) {
        MultiDex.install(this)
        super.attachBaseContext(base)
    }

    companion object {
        var appContext: Context? = null
        private var singleton: TV9Base? = null
        val instance: TV9Base?
            get() {
                if (singleton == null) {
                    singleton = TV9Base()
                }
                return singleton
            }
    }
}