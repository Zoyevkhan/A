package com.tv9news.utils.networks


import java.io.IOException

class NoConnectivityException : IOException() {
    override val message: String?
        get() = "Please connect with internet"

}