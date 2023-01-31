package com.tv9news.utils.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tv9news.utils.helpers.UIEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var baseExceptionHandler: CoroutineExceptionHandler
    var context: Context = application.applicationContext

    val baseUIEventChannel = Channel<UIEvent>(Channel.UNLIMITED)
    val baseEvents = baseUIEventChannel.receiveAsFlow()

    init {
        inilizeExceptionHandlers()
    }

    fun inilizeExceptionHandlers() {
        baseExceptionHandler = CoroutineExceptionHandler { _, exception ->
            sendSameEvents(UIEvent.Exception(exception))
        }
    }

    fun sendSameEvents(event: UIEvent) {
        sendLoadingStateToUIs(event)
    }

    fun sendLoadingStateToUIs(
        apiEvent: UIEvent = UIEvent.NONE
    ) {
        viewModelScope.launch {
            baseUIEventChannel.send(apiEvent)
        }
    }

}