package com.aditya.wall.data.repository

import android.util.Log
import com.aditya.wall.domain.model.Post
import com.aditya.wall.domain.model.Profile
import com.aditya.wall.domain.repository.HomeRepository
import com.aditya.wall.utils.NetworkResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val fAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): HomeRepository {
    override suspend fun addPost(post: Post): Flow<NetworkResponse<String>> = callbackFlow {
        try {
            if (post.userId == "" || post.content == "" || post.time == "" || post.userName == "") {
                Log.w("HomeRepository", "addPost:failure -> invalid post")
                trySend(NetworkResponse.Failure("Failure"))
            }
            else {
                firestore.collection("posts")
                    .document()
                    .set(post)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.d("HomeRepository", "addPost:success")
                            trySend(NetworkResponse.Success("Success"))
                        }
                        else {
                            Log.w("HomeRepository", "addPost:failure", it.exception)
                            trySend(NetworkResponse.Failure(it.exception?.message.toString()))
                        }
                    }
            }
        }
        catch (e: Exception) {
            Log.w("HomeRepository", "getProfile:failure", e)
            trySend(NetworkResponse.Failure(e.message.toString()))
        }

        awaitClose {
            close()
        }
    }

    override suspend fun getPosts(): Flow<NetworkResponse<List<Post>>> = callbackFlow {
        try {
            trySend(NetworkResponse.Loading())
            val posts: ArrayList<Post> = arrayListOf()

            firestore.collection("posts")
                .orderBy("time", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .addSnapshotListener{ snap, error ->
                    if (error != null) {
                        Log.w("HomeRepository", "getPosts:failure", error)
                        trySend(NetworkResponse.Failure(error.message.toString()))
                        return@addSnapshotListener
                    }

                    if (snap != null && !snap.isEmpty) {
                        val response = snap.toObjects(Post::class.java)
                        Log.d("HomeRepository", "getPosts:success")
                        posts.clear()
                        for (post in response){
                            posts.add(post)
                        }
                        trySend(NetworkResponse.Success(posts))
                    } else {
                        Log.w("HomeRepository", "getPosts: no such document")
                        trySend(NetworkResponse.Success(arrayListOf()))
                    }
                }
        }
        catch (e: Exception) {
            Log.w("HomeRepository", "getProfile:failure", e)
            trySend(NetworkResponse.Failure(e.message.toString()))
        }

        awaitClose {
            close()
        }
    }

    override suspend fun filterPosts(userId: String): Flow<NetworkResponse<List<Post>>> = callbackFlow {
        try {
            trySend(NetworkResponse.Loading())
            val posts: ArrayList<Post> = arrayListOf()

            firestore.collection("posts")
                .whereEqualTo("userId", userId)
                .orderBy("time", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .addSnapshotListener{ snap, error ->
                    if (error != null) {
                        Log.w("HomeRepository", "filterPosts:failure", error)
                        trySend(NetworkResponse.Failure(error.message.toString()))
                        return@addSnapshotListener
                    }

                    if (snap != null && !snap.isEmpty) {
                        val response = snap.toObjects(Post::class.java)
                        Log.d("HomeRepository", "filterPosts:success")
                        posts.clear()
                        for (post in response){
                            posts.add(post)
                        }
                        trySend(NetworkResponse.Success(posts))
                    } else {
                        Log.w("HomeRepository", "filterPosts: no such document")
                        trySend(NetworkResponse.Success(arrayListOf()))
                    }
                }
        }
        catch (e: Exception) {
            Log.w("HomeRepository", "filterPosts:failure", e)
            trySend(NetworkResponse.Failure(e.message.toString()))
        }

        awaitClose {
            close()
        }
    }

    override suspend fun logOut(): Flow<NetworkResponse<String>> = callbackFlow {
        try {
            fAuth.signOut()
            Log.d("HomeRepository", "logOut:success")
            trySend(NetworkResponse.Success("Success"))
        }
        catch (e: Exception) {
            Log.w("HomeRepository", "logOut:failure", e)
            trySend(NetworkResponse.Failure(e.message.toString()))
        }

        awaitClose {
            close()
        }
    }

    override suspend fun getProfile(): Flow<NetworkResponse<Profile>> = callbackFlow {
        try {
            firestore.collection("users").document(fAuth.currentUser!!.uid).get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val profile = it.result.toObject(Profile::class.java)
                        Log.d("HomeRepository", "getProfile:success")

                        profile?.let { p ->
                            trySend(NetworkResponse.Success(p))
                        }
                    }
                    else {
                        Log.w("HomeRepository", "getProfile:failure", it.exception)
                        trySend(NetworkResponse.Failure(it.exception?.message.toString()))
                    }
                }
        }
        catch (e: Exception) {
            Log.w("HomeRepository", "getProfile:failure", e)
            trySend(NetworkResponse.Failure(e.message.toString()))
        }

        awaitClose {
            close()
        }
    }
}