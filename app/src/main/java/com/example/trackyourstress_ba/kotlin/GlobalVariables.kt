package com.example.trackyourstress_ba.kotlin

import java.util.logging.LogManager
import java.util.logging.Logger

class GlobalVariables  {
    companion object {
        var apiEndPoint = "https://api.trackyourstress.org"
        var cur_language = "de"
        //var platform = "stress"
        var localStorage = HashMap<String, String>()
        //var studyIDs = ArrayList<Int>()
        //var questionnaireIDs = ArrayList<Int>()
        var logger = LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME)
    }
}
