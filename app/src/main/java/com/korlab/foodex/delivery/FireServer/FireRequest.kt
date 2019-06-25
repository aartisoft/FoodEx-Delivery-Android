package com.korlab.foodex.delivery.FireServer

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.functions.FirebaseFunctions
import com.korlab.foodex.delivery.Technical.Helper
import org.json.JSONObject

class FireRequest {


    companion object {
        private var functions: FirebaseFunctions = FirebaseFunctions.getInstance()

        fun getData(collectionName: String, documentId: String, onSuccess: (hashMap: HashMap<String, Object>) -> Unit, onFail: () -> Unit) {
            Helper.log("Get documentId: " + documentId + " from collection: " + collectionName)
            Database.readValue(collectionName, documentId) { documentSnapshot: DocumentSnapshot? ->
                val hashMap = documentSnapshot?.data as HashMap<String, Object>?
                if (hashMap == null) {
                    onFail()
                } else {
                    Helper.log("Response from FireStore " + documentSnapshot?.data!!)
                    onSuccess(hashMap)
                }
            }
        }

        fun callFunction(functionName: String, dataHashMap: java.util.HashMap<String, Any>, onSuccess: (hashMap: HashMap<String, Object>) -> Unit, onFail: () -> Unit): Task<String> {
            Helper.log("Call function: $functionName")
            Helper.log("Send data: $dataHashMap")
            return functions
                    .getHttpsCallable(functionName)
                    .call(dataHashMap)
                    .continueWith { task ->
                        try {
                            val hashMap = task.result!!.data as HashMap<String, Object>?
                            Helper.log("$functionName result hashMap: $hashMap")
                            val obj = JSONObject(hashMap)
                            val code: Int = obj.getString("code").toInt()
//                            val text = obj.getString("text")
                            Helper.log("code: $code")
//                            Helper.log("text: $text")
                            if (code == 200) {
                                onSuccess(hashMap!!)
                            } else {
                                onFail()
                            }
                        } catch (e: Exception) {
                            Helper.log("Error callFunction: $e")
                            onFail()
                        }
                        "0"
                    }
        }
    }
}