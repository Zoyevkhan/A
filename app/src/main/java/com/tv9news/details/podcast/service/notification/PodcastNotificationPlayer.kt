package com.tv9news.details.service.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.Notification.VISIBILITY_PUBLIC
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tv9news.R
import com.tv9news.details.PodcastPlayerManager
import com.tv9news.details.PodcastPlayerManagerListener
import com.tv9news.details.general.PlayerUtil
import com.tv9news.details.podcast.general.PodcastStatus
import java.lang.ref.WeakReference

class PodcastNotificationPlayer private constructor(private val context: Context) :
    PodcastPlayerManagerListener {

    private var title: String? = null
    private var time = "00:00"
    private var iconResource: Int = 0
    private val notificationManager: NotificationManagerCompat by lazy {
        NotificationManagerCompat.from(context)
    }
    private var notification: Notification? = null

    companion object {
        const val NEXT = "jcplayer.NEXT"
        const val PREVIOUS = "jcplayer.PREVIOUS"
        const val PAUSE = "jcplayer.PAUSE"
        const val PLAY = "jcplayer.PLAY"
        const val ACTION = "jcplayer.ACTION"
        const val PLAYLIST = "jcplayer.PLAYLIST"
        const val CURRENT_AUDIO = "jcplayer.CURRENT_AUDIO"

        private const val NOTIFICATION_ID = 100
        private const val NOTIFICATION_CHANNEL = "jcplayer.NOTIFICATION_CHANNEL"
        private const val NEXT_ID = 0
        private const val PREVIOUS_ID = 1
        private const val PLAY_ID = 2
        private const val PAUSE_ID = 3


        @Volatile
        private var INSTANCE: WeakReference<PodcastNotificationPlayer>? = null

        @JvmStatic
        fun getInstance(context: Context): WeakReference<PodcastNotificationPlayer> =
            INSTANCE ?: let {
                INSTANCE = WeakReference(PodcastNotificationPlayer(context))
                INSTANCE!!
            }
    }

    @SuppressLint("MissingPermission")
    fun createNotificationPlayer(title: String?, iconResourceResource: Int) {
        this.title = title
        this.iconResource = iconResourceResource
        val openUi = Intent(context, context.javaClass)
        openUi.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

        notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
            .setSmallIcon(iconResourceResource)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, iconResourceResource))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContent(createNotificationPlayerView())
            .setSound(null)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    NOTIFICATION_ID,
                    openUi,
                    buildIntentFlags()
                )
            )
            .setAutoCancel(false)
            .build()

        @RequiresApi(Build.VERSION_CODES.O)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL,
                NOTIFICATION_CHANNEL,
                NotificationManager.IMPORTANCE_LOW
            )
            channel.lockscreenVisibility = VISIBILITY_PUBLIC
            channel.enableLights(false)
            channel.enableVibration(false)
            channel.setSound(null, null)

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        notification?.let { notificationManager.notify(NOTIFICATION_ID, it) }
    }

    fun updateNotification() {
        createNotificationPlayer(title, iconResource)
    }

    private fun createNotificationPlayerView(): RemoteViews {
        val remoteView: RemoteViews

        if (PodcastPlayerManager.getInstance(context).get()?.isPaused() == true) {
            remoteView = RemoteViews(context.packageName, R.layout.layout_paused_notification)
            remoteView.setOnClickPendingIntent(
                R.id.btn_play_notification,
                buildPendingIntent(PLAY, PLAY_ID)
            )
        } else {
            remoteView = RemoteViews(context.packageName, R.layout.layout_playing_notification)
            remoteView.setOnClickPendingIntent(
                R.id.btn_pause_notification,
                buildPendingIntent(PAUSE, PAUSE_ID)
            )
        }

        remoteView.setTextViewText(R.id.txt_current_music_notification, title)
        remoteView.setTextViewText(R.id.txt_duration_notification, time)
        remoteView.setImageViewResource(R.id.icon_player, iconResource)
        remoteView.setOnClickPendingIntent(
            R.id.btn_next_notification,
            buildPendingIntent(NEXT, NEXT_ID)
        )
        remoteView.setOnClickPendingIntent(
            R.id.btn_prev_notification,
            buildPendingIntent(PREVIOUS, PREVIOUS_ID)
        )

        return remoteView
    }

    private fun buildIntentFlags(): Int =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

    private fun buildPendingIntent(action: String, id: Int): PendingIntent {
        val playIntent =
            Intent(context.applicationContext, PodcastPlayerNotificationReceiver::class.java)
        playIntent.putExtra(ACTION, action)

        return PendingIntent.getBroadcast(
            context.applicationContext,
            id,
            playIntent,
            buildIntentFlags()
        )
    }

    override fun onPreparedAudio(status: PodcastStatus) {

    }

    override fun onCompletedAudio() {

    }

    override fun onPaused(status: PodcastStatus) {
        createNotificationPlayer(title, iconResource)
    }

    override fun onStopped(status: PodcastStatus) {
        destroyNotificationIfExists()
    }

    override fun onContinueAudio(status: PodcastStatus) {}

    override fun onPlaying(status: PodcastStatus) {
        createNotificationPlayer(title, iconResource)
    }

    override fun onTimeChanged(status: PodcastStatus) {
        this.time = PlayerUtil.toTimeSongString(status.currentPosition.toInt())
        this.title = status.jcAudio.article_title
        createNotificationPlayer(title, iconResource)
    }


    fun destroyNotificationIfExists() {
        try {
            notificationManager.cancel(NOTIFICATION_ID)
            notificationManager.cancelAll()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    override fun onJcpError(throwable: Throwable) {

    }
}