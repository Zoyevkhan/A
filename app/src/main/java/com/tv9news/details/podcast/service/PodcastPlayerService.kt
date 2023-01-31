package com.tv9news.details.service

import android.app.Service
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder

import com.tv9news.details.podcast.general.PodcastStatus
import com.tv9news.details.podcast.general.Origin
import com.tv9news.details.podcast.general.errors.AudioAssetsInvalidException
import com.tv9news.details.podcast.general.errors.AudioFilePathInvalidException
import com.tv9news.details.podcast.general.errors.AudioRawInvalidException
import com.tv9news.details.podcast.general.errors.AudioUrlInvalidException
import com.tv9news.models.home.Lists
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class PodcastPlayerService : Service(), MediaPlayer.OnPreparedListener,
    MediaPlayer.OnCompletionListener,
    MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnErrorListener {

    private val binder = JcPlayerServiceBinder()

    private var mediaPlayer: MediaPlayer? = null

    var isPlaying: Boolean = false
        private set

    var isPaused: Boolean = true
        private set

    var currentAudio: Lists? = null
        private set

    private val jcStatus = PodcastStatus()

    private var assetFileDescriptor: AssetFileDescriptor? = null // For Asset and Raw file.

    var serviceListener: PodcastPlayerServiceListener? = null


    inner class JcPlayerServiceBinder : Binder() {
        val service: PodcastPlayerService
            get() = this@PodcastPlayerService
    }

    override fun onBind(intent: Intent): IBinder = binder


    fun play(jcAudio: Lists): PodcastStatus {
        val tempJcAudio = currentAudio
        currentAudio = jcAudio
        var status = PodcastStatus()

        if (isAudioFileValid(jcAudio.media_url!!,  Origin.URL)) {
            try {
                mediaPlayer?.let {
                    if (isPlaying) {
                        stop()
                        play(jcAudio)
                    } else {
                        if (tempJcAudio !== jcAudio) {
                            stop()
                            play(jcAudio)
                        } else {
                            status = updateStatus(jcAudio, PodcastStatus.PlayState.CONTINUE)
                            updateTime()
                            serviceListener?.onContinueListener(status)
                        }
                    }
                } ?: let {
                    mediaPlayer = MediaPlayer().also {
                        when ( Origin.URL) {
                            Origin.URL -> it.setDataSource(jcAudio.media_url)
                            Origin.RAW -> assetFileDescriptor =
                                applicationContext.resources.openRawResourceFd(
                                    Integer.parseInt(jcAudio.media_url)
                                ).also { descriptor ->
                                    it.setDataSource(
                                        descriptor.fileDescriptor,
                                        descriptor.startOffset,
                                        descriptor.length
                                    )
                                    descriptor.close()
                                    assetFileDescriptor = null
                                }
                            Origin.ASSETS -> {
                                assetFileDescriptor = applicationContext.assets.openFd(jcAudio.media_url!!)
                                    .also { descriptor ->
                                        it.setDataSource(
                                            descriptor.fileDescriptor,
                                            descriptor.startOffset,
                                            descriptor.length
                                        )

                                        descriptor.close()
                                        assetFileDescriptor = null
                                    }
                            }
                            Origin.FILE_PATH ->
                                it.setDataSource(applicationContext, Uri.parse(jcAudio.media_url))
                        }

                        it.prepareAsync()
                        it.setOnPreparedListener(this)
                        it.setOnBufferingUpdateListener(this)
                        it.setOnCompletionListener(this)
                        it.setOnErrorListener(this)

                        status = updateStatus(jcAudio, PodcastStatus.PlayState.PREPARING)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            throwError(jcAudio.media_url,  Origin.URL)
        }

        return status
    }

    fun pause(jcAudio: Lists): PodcastStatus {
        val status = updateStatus(jcAudio, PodcastStatus.PlayState.PAUSE)
        serviceListener?.onPausedListener(status)
        return status
    }

    fun stop(): PodcastStatus {
        val status = updateStatus(status = PodcastStatus.PlayState.STOP)
        serviceListener?.onStoppedListener(status)
        return status
    }


    fun seekTo(time: Int) {
        mediaPlayer?.seekTo(time)
    }

    override fun onBufferingUpdate(mediaPlayer: MediaPlayer, i: Int) {}

    override fun onCompletion(mediaPlayer: MediaPlayer) {
        serviceListener?.onCompletedListener()
    }

    override fun onError(mediaPlayer: MediaPlayer, i: Int, i1: Int): Boolean {
        return false
    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        this.mediaPlayer = mediaPlayer
        val status = updateStatus(currentAudio, PodcastStatus.PlayState.PLAY)

        updateTime()
        serviceListener?.onPreparedListener(status)
    }

    private fun updateStatus(
        jcAudio: Lists? = null,
        status: PodcastStatus.PlayState
    ): PodcastStatus {
        currentAudio = jcAudio
        jcStatus.jcAudio = jcAudio
        jcStatus.playState = status

        mediaPlayer?.let {
            jcStatus.duration = it.duration.toLong()
            jcStatus.currentPosition = it.currentPosition.toLong()
        }

        when (status) {
            PodcastStatus.PlayState.PLAY -> {
                try {
                    mediaPlayer?.start()
                    isPlaying = true
                    isPaused = false

                } catch (exception: Exception) {
                    serviceListener?.onError(exception)
                }
            }

            PodcastStatus.PlayState.STOP -> {
                mediaPlayer?.let {
                    it.stop()
                    it.reset()
                    it.release()
                    mediaPlayer = null
                }

                isPlaying = false
                isPaused = true
            }

            PodcastStatus.PlayState.PAUSE -> {
                mediaPlayer?.pause()
                isPlaying = false
                isPaused = true
            }

            PodcastStatus.PlayState.PREPARING -> {
                isPlaying = false
                isPaused = true
            }

            PodcastStatus.PlayState.PLAYING -> {
                isPlaying = true
                isPaused = false
            }

            else -> { // CONTINUE case
                mediaPlayer?.start()
                isPlaying = true
                isPaused = false
            }
        }

        return jcStatus
    }

    private fun updateTime() {
        object : Thread() {
            override fun run() {
                while (isPlaying) {
                    try {
                        val status = updateStatus(currentAudio, PodcastStatus.PlayState.PLAYING)
                        serviceListener?.onTimeChangedListener(status)
                        sleep(TimeUnit.SECONDS.toMillis(1))
                    } catch (e: IllegalStateException) {
                        e.printStackTrace()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }
                }
            }
        }.start()
    }

    private fun isAudioFileValid(path: String, origin: Origin): Boolean {
        when (origin) {
            Origin.URL -> return path.startsWith("http") || path.startsWith("https")

            Origin.RAW -> {
                assetFileDescriptor = null
                assetFileDescriptor =
                    applicationContext.resources.openRawResourceFd(Integer.parseInt(path))
                return assetFileDescriptor != null
            }

            Origin.ASSETS -> return try {
                assetFileDescriptor = null
                assetFileDescriptor = path?.let { applicationContext.assets.openFd(it) }
                assetFileDescriptor != null
            } catch (e: IOException) {
                e.printStackTrace() //TODO: need to give user more readable error.
                false
            }

            Origin.FILE_PATH -> {
                val file = File(path)
                //TODO: find an alternative to checking if file is exist, this code is slower on average.
                //read more: http://stackoverflow.com/a/8868140
                return file.exists()
            }

            else -> // We should never arrive here.
                return false // We don't know what the origin of the Audio File
        }
    }

    private fun throwError(path: String?, origin: Origin) {
        when (origin) {
            Origin.URL -> throw AudioUrlInvalidException(path)

            Origin.RAW -> try {
                throw AudioRawInvalidException(path)
            } catch (e: AudioRawInvalidException) {
                e.printStackTrace()
            }

            Origin.ASSETS -> try {
                throw AudioAssetsInvalidException(path)
            } catch (e: AudioAssetsInvalidException) {
                e.printStackTrace()
            }

            Origin.FILE_PATH -> try {
                throw AudioFilePathInvalidException(path)
            } catch (e: AudioFilePathInvalidException) {
                e.printStackTrace()
            }
        }
    }

    fun getMediaPlayer(): MediaPlayer? {
        return mediaPlayer
    }

    fun finalize() {
        onDestroy()
        stopSelf()
    }
}
