package com.tv9news.details

import com.tv9news.details.podcast.general.PodcastStatus


interface PodcastPlayerManagerListener {

    /**
     * Prepares the new audio.
     * @param audioName The audio name.
     * @param duration The audio duration.
     */
    fun onPreparedAudio(status: PodcastStatus)

    /**
     * The audio ends.
     */
    fun onCompletedAudio()

    /**
     * The audio is paused.
     */
    fun onPaused(status: PodcastStatus)

    /**
     * The audio was paused and user hits play again.
     */
    fun onContinueAudio(status: PodcastStatus)

    /**
     *  Called when there is an audio playing.
     */
    fun onPlaying(status: PodcastStatus)

    /**
     * Called when the time of the audio changed.
     */
    fun onTimeChanged(status: PodcastStatus)


    /**
     * Called when the player stops.
     */
    fun onStopped(status: PodcastStatus)

    /**
     * Notifies some error.
     * @param throwable The error.
     */
    fun onJcpError(throwable: Throwable)
}