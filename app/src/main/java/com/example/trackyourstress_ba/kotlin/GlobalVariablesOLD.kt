package com.example.trackyourstress_ba.kotlin

import java.util.logging.LogManager
import java.util.logging.Logger

class GlobalVariables  {
    companion object {
        const val apiEndPoint = "https://api.trackyourstress.org"
        var cur_language = "de"
        var localStorage = HashMap<String, String>()
        var logger = LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME)
        var clientName = "track-your-stress 1.0.0"
    }
}
