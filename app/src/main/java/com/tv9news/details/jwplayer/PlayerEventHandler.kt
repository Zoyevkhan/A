package com.tv9news.details.jwplayer

import android.util.Log
import com.jwplayer.pub.api.JWPlayer
import com.jwplayer.pub.api.events.EventType
import com.jwplayer.pub.api.events.PipCloseEvent
import com.jwplayer.pub.api.events.PipOpenEvent
import com.jwplayer.pub.api.events.listeners.PipPluginEvents.OnPipCloseListener
import com.jwplayer.pub.api.events.listeners.PipPluginEvents.OnPipOpenListener

class PlayerEventHandler(mPlayer: JWPlayer) : OnPipOpenListener,
    OnPipCloseListener {
    init {
        mPlayer.addListeners(this, EventType.PIP_OPEN, EventType.PIP_CLOSE)
    }

    override fun onPipOpen(pipOpenEvent: PipOpenEvent) {
        //PiP open is triggered
        Log.d("OnPipOpenListener", "onPipOpen")
    }

    override fun onPipClose(pipCloseEvent: PipCloseEvent) {
        //PiP Close is Triggered
        Log.d("OnPipOpenListener", "onPipClose")
    }
}