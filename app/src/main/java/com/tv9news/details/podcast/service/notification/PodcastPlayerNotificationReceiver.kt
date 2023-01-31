package com.tv9news.details.service.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tv9news.details.PodcastPlayerManager
import com.tv9news.details.podcast.general.errors.AudioListNullPointerException

class PodcastPlayerNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val jcPlayerManager = PodcastPlayerManager.getInstance(context)
        var action: String? = ""

        if (intent.hasExtra(PodcastNotificationPlayer.ACTION)) {
            action = intent.getStringExtra(PodcastNotificationPlayer.ACTION)
        }

        when (action) {
            PodcastNotificationPlayer.PLAY -> try {
                jcPlayerManager.get()?.continueAudio()
                jcPlayerManager.get()?.updateNotification()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            PodcastNotificationPlayer.PAUSE -> try {
                jcPlayerManager.get()?.pauseAudio()
                jcPlayerManager.get()?.updateNotification()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            PodcastNotificationPlayer.NEXT -> try {
                jcPlayerManager.get()?.nextAudio()
            } catch (e: AudioListNullPointerException) {
                try {
                    jcPlayerManager.get()?.continueAudio()
                } catch (e1: AudioListNullPointerException) {
                    e1.printStackTrace()
                }

            }

            PodcastNotificationPlayer.PREVIOUS -> try {
                jcPlayerManager.get()?.previousAudio()
            } catch (e: Exception) {
                try {
                    jcPlayerManager.get()?.continueAudio()
                } catch (e1: AudioListNullPointerException) {
                    e1.printStackTrace()
                }
            }

        }
    }
}
