package com.aditya.wall.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import com.aditya.wall.domain.repository.SignUpRepository
import com.aditya.wall.utils.DateTimeUtils
import com.aditya.wall.utils.NetworkResponse
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.type.DateTime
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUpRepositoryImpl @Inject constructor(
    private val fAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): SignUpRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun signUpWithGoogle(credential: Credential): Flow<NetworkResponse<String>> = callbackFlow {

        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val authCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

            fAuth.signInWithCredential(authCredential)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val uid = fAuth.currentUser?.uid
                        if (uid != null) {
                            firestore.collection("users").document(uid).set(
                                hashMapOf(
                                    "userId" to uid,
                                    "userName" to googleIdTokenCredential.displayName,
                                    "lastLogin" to LocalDateTime.now().format(DateTimeUtils.formatter)
                                )
                            )
                        }
                        Log.w("SignUpRepository", "signInWithCredential:success")
                        trySend(NetworkResponse.Success("Success"))
                    } else {
                        Log.w("SignUpRepository", "signInWithCredential:failure")
                        trySend(NetworkResponse.Failure(it.exception?.message.toString()))
                    }
                }

        }
        else {
            Log.w("SignUpRepository", "Credential is not GoogleIdTokenCredential")
        }

        awaitClose {
            close()
        }
    }
}