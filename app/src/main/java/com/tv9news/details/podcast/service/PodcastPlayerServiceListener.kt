package com.tv9news.details.service

import com.tv9news.details.podcast.general.PodcastStatus

interface PodcastPlayerServiceListener {

    /**
     * Notifies on prepared audio for the service listeners
     */
    fun onPreparedListener(status: PodcastStatus)

    /**
     * Notifies on time changed for the service listeners
     */
    fun onTimeChangedListener(status: PodcastStatus)

    /**
     * Notifies on continue for the service listeners
     */
    fun onContinueListener(status: PodcastStatus)

    /**
     * Notifies on completed audio for the service listeners
     */
    fun onCompletedListener()

    /**
     * Notifies on paused for the service listeners
     */
    fun onPausedListener(status: PodcastStatus)

    /**
     * Notifies on stopped for the service listeners
     */
    fun onStoppedListener(status: PodcastStatus)

    /**
     * Notifies an error for the service listeners
     */
    fun onError(exception: Exception)
}