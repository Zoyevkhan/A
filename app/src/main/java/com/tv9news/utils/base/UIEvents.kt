package com.tv9news.utils.helpers

sealed class UIEvent {
    object onLoading : UIEvent()
    class Success(val message: String) : UIEvent()
    class OnError(val message: String) : UIEvent()
    class Exception(val exception: Throwable) : UIEvent()
    object NONE : UIEvent()
}
