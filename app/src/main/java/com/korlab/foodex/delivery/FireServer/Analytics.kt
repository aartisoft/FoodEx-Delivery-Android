package com.korlab.foodex.FireServer

import android.os.Bundle
import android.util.Log
import com.google.android.gms.tasks.Task

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.functions.FirebaseFunctions
import org.json.JSONObject


object Analytics {
//    private val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(ScannerActivity.context)
//
//    enum class Event
//    {
//        AppOpen
//    }
//
//    fun logEvent(event: Event)
//    {
//        Log.d("FireBase", "LogEvent: $event")
//        val bundle = Bundle()
//        when (event)
//        {
//            Event.AppOpen ->
//            {
//                bundle.putString(FirebaseAnalytics.Param.LOCATION, "Mariupol")
//
//                Log.d("FireBase", "bundle: " + bundle.toString())
//                firebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)
//            }
//        }
//
//        Log.d("FireBase", "done")
//    }

    private var functions: FirebaseFunctions = FirebaseFunctions.getInstance()

    fun pingStartApp(isWorker: Boolean): Task<String> {
        Log.d("UIDebug", "pingStartApp()")
        val data = java.util.HashMap<String, Any>()
        data["isWorker"] = isWorker

        return functions
                .getHttpsCallable("pingStartApp")
                .call(data)
                .continueWith { task ->
                    Log.d("UIDebug", "continueWith")
                    try {
                        val result: java.util.HashMap<String, String> = task.result!!.data as java.util.HashMap<String, String>
                        Log.d("UIDebug", "result: $result")
                        val obj = JSONObject(result)
                        Log.d("UIDebug", "code: " + obj.getString("code") + " text: " + obj.getString("text"))
                    } catch (e: Exception) {
                        Log.d("UIDebug", "error: $e")
                    }
                    "0"
                }
    }
}