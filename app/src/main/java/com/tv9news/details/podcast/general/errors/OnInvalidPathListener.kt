package com.tv9news.details.podcast.general.errors

import com.tv9news.models.home.Lists

interface OnInvalidPathListener {

    /**
     * Audio path error jcPlayerManagerListener.
     * @param jcAudio The wrong audio.
     */
    fun onPathError(jcAudio: Lists)
}