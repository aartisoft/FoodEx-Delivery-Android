package com.korlab.foodex.FireServer

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage

object Storage {
    private val storage: FirebaseStorage = FirebaseStorage.getInstance("gs://foodex-korlab.appspot.com/")
    fun uploadImage(bytes: ByteArray, path: String) {
        val riversRef = storage.reference.child("images/$path")
        riversRef.putBytes(bytes)
                .addOnFailureListener { Log.d("UIDebug", "Upload ERROR") }
                .addOnSuccessListener { Log.d("UIDebug", "Uploaded") }
    }

    fun uploadImageOrder(bytes: ByteArray, path: String, onCompleteEvent: () -> Unit) {
        val riversRef = storage.reference.child("images/$path")
        riversRef.putBytes(bytes)
                .addOnFailureListener { Log.d("UIDebug", "Upload ERROR") }
                .addOnSuccessListener {
                    Log.d("UIDebug", "Uploaded")
                    onCompleteEvent()
                }
    }

    fun downloadImage(serverPath: String, onCompleteEvent: (bytes: ByteArray) -> Unit, onErrorEvent: () -> Unit) {
        val megabyte = (1024 * 1024 * 15).toLong()
        Log.d("UIDebug", "Download: images/$serverPath")

        storage.reference.child("images/$serverPath").getBytes(megabyte).addOnSuccessListener { bytes ->
            Log.d("UIDebug", "Download complete: $serverPath")
            onCompleteEvent(bytes)
        }.addOnFailureListener { exception ->
            Log.d("UIDebug", "Download ERROR: $serverPath")
            onErrorEvent()
        }
    }

    fun downloadImageWorker(serverPath: String, id: String, onCompleteEvent: (bytes: ByteArray, id: String) -> Unit, onErrorEvent: (id: String) -> Unit) {
        val megabyte = (1024 * 1024 * 15).toLong()
        storage.reference.child("images/$serverPath/$id.jpg").getBytes(megabyte).addOnSuccessListener { bytes ->
            Log.d("UIDebug", "Download complete: $serverPath")
            onCompleteEvent(bytes, id)
        }.addOnFailureListener { exception ->
            Log.d("UIDebug", "Download ERROR: $serverPath")
            onErrorEvent(id)
        }
    }
}

