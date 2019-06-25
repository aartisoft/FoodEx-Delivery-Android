package com.korlab.foodex.delivery.FireServer

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.korlab.foodex.delivery.Technical.Helper


object Database {
    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()


    fun readValue(collection: String, documentId: String, onSuccess: (dataSnapshot: DocumentSnapshot?) -> Unit) {
        val docRef = database.collection(collection).document(documentId)
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        onSuccess(document)
                        Helper.log("DocumentSnapshot data: ${document.data}")
                    } else {
                        Helper.log("No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Helper.log("Get failed with: $exception")
                }
    }
}