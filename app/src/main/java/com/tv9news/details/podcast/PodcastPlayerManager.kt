package com.tv9news.details

import android.content.Context
import com.tv9news.details.general.errors.PodcastServiceDisconnectedError

import com.tv9news.details.podcast.general.PodcastStatus
import com.tv9news.details.podcast.general.errors.AudioListNullPointerException
import com.tv9news.details.service.PodcastPlayerService
import com.tv9news.details.service.PodcastPlayerServiceListener
import com.tv9news.details.service.PodcastServiceConnection
import com.tv9news.details.service.notification.PodcastNotificationPlayer
import com.tv9news.models.home.Lists
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

class PodcastPlayerManager
private constructor(private val serviceConnection: PodcastServiceConnection) :
    PodcastPlayerServiceListener {

    lateinit var context: Context
    private var jcNotificationPlayer: PodcastNotificationPlayer? = null
    private var jcPlayerService: PodcastPlayerService? = null
    private var serviceBound = false
    var playlist: ArrayList<Lists> = ArrayList()
    private var currentPositionList: Int = 0
    private val managerListeners: CopyOnWriteArrayList<PodcastPlayerManagerListener> =
        CopyOnWriteArrayList()

    var jcPlayerManagerListener: PodcastPlayerManagerListener? = null
        set(value) {
            value?.let {
                if (managerListeners.contains(it).not()) {
                    managerListeners.add(it)
                }
            }
            field = value
        }

    val currentAudio: Lists?
        get() = jcPlayerService?.currentAudio

    var currentStatus: PodcastStatus? = null
        private set

    var onShuffleMode: Boolean = false

    var repeatPlaylist: Boolean = false
        private set

    var repeatCurrAudio: Boolean = false
        private set

    private var repeatCount = 0

    init {
        initService()
    }

    companion object {

        @Volatile
        private var INSTANCE: WeakReference<PodcastPlayerManager>? = null

        @JvmStatic
        fun getInstance(
            context: Context,
            playlist: ArrayList<Lists>? = null,
            listener: PodcastPlayerManagerListener? = null
        ): WeakReference<PodcastPlayerManager> = INSTANCE ?: let {
            INSTANCE = WeakReference(
                PodcastPlayerManager(PodcastServiceConnection(context)).also {
                    it.context = context
                    it.playlist = playlist ?: ArrayList()
                    it.jcPlayerManagerListener = listener
                }
            )
            INSTANCE!!
        }
    }

    /**
     * Connects with audio service.
     */
    private fun initService(connectionListener: ((service: PodcastPlayerService?) -> Unit)? = null) =
        serviceConnection.connect(
            playlist = playlist,
            onConnected = { binder ->
                jcPlayerService = binder?.service.also { service ->
                    serviceBound = true
                    connectionListener?.invoke(service)
                } ?: throw PodcastServiceDisconnectedError
            },
            onDisconnected = {
                serviceBound = false
                throw PodcastServiceDisconnectedError
            }
        )

    /**
     * Plays the given [JcAudio].
     * @param jcAudio The audio to be played.
     */
    @Throws(AudioListNullPointerException::class)
    fun playAudio(jcAudio: Lists) {
        if (playlist.isEmpty()) {
            notifyError(AudioListNullPointerException())
        } else {
            jcPlayerService?.let { service ->
                service.serviceListener = this
                service.play(jcAudio)
            } ?: initService { service ->
                jcPlayerService = service
                playAudio(jcAudio)
            }
        }
    }

    private val seekForwardTime = 15 * 1000 // default 15 second
    private val seekBackwardTime = 15 * 1000 // default 15 second
    /**
     * Goes to forward audio.
     */
    @Throws(AudioListNullPointerException::class)
    fun forwardAudio() {
        if (playlist.isEmpty()) {
            throw AudioListNullPointerException()
        } else {
            jcPlayerService?.let { service ->
                if (service.getMediaPlayer() != null) {
                    val currentPosition: Int = service.getMediaPlayer()!!.getCurrentPosition()
                    if (currentPosition + seekForwardTime <= service.getMediaPlayer()!!.getDuration()) {
                        service.getMediaPlayer()!!.seekTo(currentPosition + seekForwardTime)
                    } else {
                        service.getMediaPlayer()!!.seekTo(service.getMediaPlayer()!!.getDuration())
                    }
                }
            }
        }
    }


    /**
     * Goes to rewind audio.
     */
    @Throws(AudioListNullPointerException::class)
    fun rewindAudio() {
        if (playlist.isEmpty()) {
            throw AudioListNullPointerException()
        } else {
            jcPlayerService?.let { service ->
                if (service.getMediaPlayer() != null) {
                    val currentPosition: Int = service.getMediaPlayer()!!.currentPosition
                    if (currentPosition - seekBackwardTime >= 0) {
                        service.getMediaPlayer()!!.seekTo(currentPosition - seekBackwardTime)
                    } else {
                        service.getMediaPlayer()!!.seekTo(0)
                    }
                }
            }
        }
    }

    /**
     * Goes to next audio.
     */
    @Throws(AudioListNullPointerException::class)
    fun nextAudio() {
        if (playlist.isEmpty()) {
            throw AudioListNullPointerException()
        } else {
            jcPlayerService?.let { service ->
                if (repeatCurrAudio) {
                    currentAudio?.let {
                        service.seekTo(0)
                        service.onPrepared(service.getMediaPlayer()!!)
                    }
                } else {
                    service.stop()
                    getNextAudio()?.let { service.play(it) } ?: service.finalize()
                }
            }
        }
    }

    /**
     * Goes to previous audio.
     */
    @Throws(AudioListNullPointerException::class)
    fun previousAudio() {
        if (playlist.isEmpty()) {
            throw AudioListNullPointerException()
        } else {
            jcPlayerService?.let { service ->
                if (repeatCurrAudio) {
                    currentAudio?.let {
                        service.seekTo(0)
                        service.onPrepared(service.getMediaPlayer()!!)
                    }
                } else {
                    service.stop()
                    getPreviousAudio().let { service.play(it) }
                }
            }
        }
    }

    /**
     * Pauses the current audio.
     */
    fun pauseAudio() {
        jcPlayerService?.let { service -> currentAudio?.let { service.pause(it) } }
    }

    /**
     * Continues the stopped audio.
     */
    @Throws(AudioListNullPointerException::class)
    fun continueAudio() {
        if (playlist.isEmpty()) {
            throw AudioListNullPointerException()
        } else {
            val audio = jcPlayerService?.currentAudio ?: let { playlist.first() }
            playAudio(audio)
        }
    }

    /**
     * Creates a new notification with icon resource.
     * @param iconResource The icon resource path.
     */
    fun createNewNotification(iconResource: Int) {
        jcNotificationPlayer
            ?.createNotificationPlayer(currentAudio?.article_title, iconResource)
            ?: let {
                jcNotificationPlayer = PodcastNotificationPlayer
                    .getInstance(context)
                    .get()
                    .also { notification ->
                        jcPlayerManagerListener = notification
                    }

                createNewNotification(iconResource)
            }
    }

    /**
     * Updates the current notification
     */
    fun updateNotification() {
        jcNotificationPlayer
            ?.updateNotification()
            ?: let {
                jcNotificationPlayer = PodcastNotificationPlayer
                    .getInstance(context)
                    .get()
                    .also { jcPlayerManagerListener = it }

                updateNotification()
            }
    }

    /**
     * Jumps audio to the specific time.
     */
    fun seekTo(time: Int) {
        jcPlayerService?.seekTo(time)
    }

    private fun getNextAudio(): Lists? {
        return if (onShuffleMode) {
            playlist[Random().nextInt(playlist.size)]
        } else {
            try {
                playlist[currentPositionList.inc()]
            } catch (e: IndexOutOfBoundsException) {
                if (repeatPlaylist) {
                    return playlist.first()
                }

                null
            }
        }
    }

    private fun getPreviousAudio(): Lists {
        return if (onShuffleMode) {
            playlist[Random().nextInt(playlist.size)]
        } else {
            try {
                playlist[currentPositionList.dec()]

            } catch (e: IndexOutOfBoundsException) {
                return playlist.first()
            }
        }
    }


    override fun onPreparedListener(status: PodcastStatus) {
        currentStatus = status
        updatePositionAudioList()

        for (listener in managerListeners) {
            listener.onPreparedAudio(status)
        }
    }

    override fun onTimeChangedListener(status: PodcastStatus) {
        currentStatus = status

        for (listener in managerListeners) {
            listener.onTimeChanged(status)

            if (status.currentPosition in 1..2 /* Not to call this every second */) {
                listener.onPlaying(status)
            }
        }
    }

    override fun onContinueListener(status: PodcastStatus) {
        currentStatus = status

        for (listener in managerListeners) {
            listener.onContinueAudio(status)
        }
    }

    override fun onCompletedListener() {
        for (listener in managerListeners) {
            listener.onCompletedAudio()
        }
    }

    override fun onPausedListener(status: PodcastStatus) {
        currentStatus = status

        for (listener in managerListeners) {
            listener.onPaused(status)
        }
    }

    override fun onStoppedListener(status: PodcastStatus) {
        currentStatus = status

        for (listener in managerListeners) {
            listener.onStopped(status)
        }
    }

    override fun onError(exception: Exception) {
        notifyError(exception)
    }

    /**
     * Notifies errors for the service listeners
     */
    private fun notifyError(throwable: Throwable) {
        for (listener in managerListeners) {
            listener.onJcpError(throwable)
        }
    }

    /**
     * Handles the repeat mode.
     */
    fun activeRepeat() {
        if (repeatCount == 0) {
            repeatPlaylist = true
            repeatCurrAudio = false
            repeatCount++
            return
        }

        if (repeatCount == 1) {
            repeatCurrAudio = true
            repeatPlaylist = false
            repeatCount++
            return
        }

        if (repeatCount == 2) {
            repeatCurrAudio = false
            repeatPlaylist = false
            repeatCount = 0
        }
    }

    /**
     * Updates the current position of the audio list.
     */
    private fun updatePositionAudioList() {
        playlist.indices
            .singleOrNull { playlist[it] == currentAudio }
            ?.let { this.currentPositionList = it }
            ?: let { this.currentPositionList = 0 }
    }

    fun isPlaying(): Boolean {
        return jcPlayerService?.isPlaying ?: false
    }

    fun isPaused(): Boolean {
        return jcPlayerService?.isPaused ?: true
    }

    /**
     * Kills the JcPlayer, including Notification and service.
     */
    fun kill() {
        jcPlayerService?.let {
            it.stop()
            it.stopSelf()
            it.stopForeground(true)
        }

        serviceConnection.disconnect()
        jcNotificationPlayer?.destroyNotificationIfExists()
        managerListeners.clear()
        INSTANCE = null
    }
}
