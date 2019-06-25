package com.korlab.foodex.delivery.FireServer

import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.korlab.foodex.delivery.Technical.Helper

import java.util.concurrent.TimeUnit


object Auth : AppCompatActivity() {
    private val TAG = "FirebaseAuth"

    private var mVerificationId: String? = null
    private var mResendToken: PhoneAuthProvider.ForceResendingToken? = null
    var isManager: Boolean = false
    var id: String = ""

    fun isUserSigned() = FirebaseAuth.getInstance().currentUser != null

    fun getUserId(): String {
        var userId: String
        if (isManager) userId = id
        else userId = FirebaseAuth.getInstance().currentUser!!.uid
        Log.d("UIDebugManager", "return userId: " + userId)
        return userId
    }

    fun getRealUserId(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun authPhone(phone: String,
                  onCorrectSent: () -> Unit,
                  errorSent: (errorText: String) -> Unit,
                  onSuccess: () -> Unit,
                  onWrong: () -> Unit) {
        Helper.log("verifyPhoneNumber: " + phone);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone, 60, TimeUnit.SECONDS, this, object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Helper.log("onVerificationCompleted")
                signInWithPhoneAuthCredential(credential, onSuccess, onWrong)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Helper.log("onVerificationFailed " + e.toString())
                errorSent(e.localizedMessage ?: "Error")
            }

            override fun onCodeSent(verificationId: String?, token: PhoneAuthProvider.ForceResendingToken?) {
                onCorrectSent()
                Helper.log("onCodeSend")
                mVerificationId = verificationId
                mResendToken = token
            }
        })
    }


    fun createEmailAndPassword(
            email: String,
            password: String,
            onSuccess: () -> Unit,
            onError: () -> Unit) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = FirebaseAuth.getInstance().currentUser
                        onSuccess()
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        onError()
                    }
                }
    }

    fun authEmailAndPassword(
            email: String,
            password: String,
            onSuccess: () -> Unit,
            onError: () -> Unit) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:success")
                        val user = FirebaseAuth.getInstance().currentUser
                        onSuccess()
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        onError()
                    }
                }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential,
                                              onSuccess: () -> Unit,
                                              onWrong: () -> Unit) {
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                Log.d(TAG, "error: " + task.exception)
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    onWrong()
                }
            }
        }
    }

    fun checkEnteredCodeVerification(code: String,
                                     onSuccess: () -> Unit,
                                     onWrong: () -> Unit) {
        Helper.log("Check entered code: $code")
        val credential = PhoneAuthProvider.getCredential(mVerificationId!!, code)
        signInWithPhoneAuthCredential(credential, onSuccess, onWrong)
    }

    fun tryEnterCodeUpdatePhone(code: String,
                                onSuccess: () -> Unit,
                                onWrong: () -> Unit) {
        Log.d("FirebaseAuth", "code: " + code)
        val credential = PhoneAuthProvider.getCredential(mVerificationId!!, code)
        updatePhoneCredential(credential, onSuccess, onWrong)
    }

    fun updatePhoneCredential(credential: PhoneAuthCredential,
                              onSuccess: () -> Unit,
                              onError: () -> Unit) {
        Log.d(TAG, "updatePhoneCredential")
        val user = FirebaseAuth.getInstance().currentUser
        user!!.updatePhoneNumber(credential).addOnCompleteListener { task ->
            Log.d(TAG, "task")
            if (task.isSuccessful) {
                Log.d(TAG, "onSuccess updatePhoneCredential")
                onSuccess()
            } else {
                Log.d(TAG, "error: " + task.exception)
                if (task.exception is FirebaseAuthUserCollisionException) {
                    // thrown if there already exists an account with the given email address
                    Log.d(TAG, "error updatePhoneCredential -> thrown if there already exists an account with the given email address")
                    onError()
                }
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    Log.d(TAG, "error updatePhoneCredential exception")
                    onError()
                }
            }
        }
    }


    fun updatePhone(newPhone: String,
                    onCorrectSent: () -> Unit,
                    errorSent: (errorText: String) -> Unit,
                    onSuccess: () -> Unit,
                    onError: () -> Unit) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(newPhone, 60, TimeUnit.SECONDS, this, object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Helper.log("onVerificationCompleted")
                updatePhoneCredential(credential, onSuccess, onError)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                errorSent(e.localizedMessage ?: "Error")
                Helper.log("onVerificationFailed " + e.toString())
            }

            override fun onCodeSent(verificationId: String?, token: PhoneAuthProvider.ForceResendingToken?) {
                onCorrectSent()
                Helper.log("onCodeSend")
                mVerificationId = verificationId
                mResendToken = token
            }
        })
    }
}