package com.tv9news.details.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Parcelable

import com.tv9news.details.service.notification.PodcastNotificationPlayer
import com.tv9news.models.home.Lists
import java.io.Serializable

/**
 * This class is an [ServiceConnection] for the [PodcastPlayerService] class.
 * @author Jean Carlos (Github: @jeancsanchez)
 * @date 15/02/18.
 * Jesus loves you.
 */
class PodcastServiceConnection(private val context: Context) : ServiceConnection {

    private var serviceBound = false
    private var onConnected: ((PodcastPlayerService.JcPlayerServiceBinder?) -> Unit)? = null
    private var onDisconnected: ((Unit) -> Unit)? = null

    override fun onServiceDisconnected(name: ComponentName?) {
        serviceBound = false
        onDisconnected?.invoke(Unit)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        serviceBound = true
        onConnected?.invoke(service as PodcastPlayerService.JcPlayerServiceBinder?)
    }

    /**
     * Connects with the [PodcastPlayerService].
     */
    fun connect(
        playlist: ArrayList<Lists>? = null,
        currentAudio: Lists? = null,
        onConnected: ((PodcastPlayerService.JcPlayerServiceBinder?) -> Unit)? = null,
        onDisconnected: ((Unit) -> Unit)? = null
    ) {
        this.onConnected = onConnected
        this.onDisconnected = onDisconnected

        if (serviceBound.not()) {
            val intent = Intent(context.applicationContext, PodcastPlayerService::class.java)
            intent.putExtra(PodcastNotificationPlayer.PLAYLIST, playlist as Serializable?)
            intent.putExtra(PodcastNotificationPlayer.CURRENT_AUDIO, currentAudio)
            context.applicationContext.bindService(intent, this, Context.BIND_AUTO_CREATE)
        }
    }

    /**
     * Disconnects from the [PodcastPlayerService].
     */
    fun disconnect() {
        if (serviceBound)
            try {
                context.unbindService(this)
            } catch (e: IllegalArgumentException) {
                //TODO: Add readable exception here
            }
    }
}
