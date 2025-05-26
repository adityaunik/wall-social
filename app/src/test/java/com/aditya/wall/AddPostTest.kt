package com.aditya.wall

import android.util.Log
import com.aditya.wall.data.repository.HomeRepositoryImpl
import com.aditya.wall.domain.model.Post
import com.aditya.wall.domain.repository.HomeRepository
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import java.lang.Exception

@org.robolectric.annotation.Config(sdk = [28])
@RunWith(org.robolectric.RobolectricTestRunner::class)
class AddPostTest {

    private val firestore = mock<FirebaseFirestore>()
    private val firebaseAuth = mock<FirebaseAuth>()
    private val collection = mock<CollectionReference>()
    private val document = mock<DocumentReference>()


    private lateinit var homeRepository: HomeRepository

    @Before
    fun setUp() {
        mockStatic(Log::class.java).use {
            `when`(Log.d(Mockito.anyString(), Mockito.anyString())).thenReturn(0)
            `when`(Log.w(Mockito.anyString(), Mockito.anyString())).thenReturn(0)
            `when`(Log.w(Mockito.anyString(), Mockito.anyString(), Mockito.any(Throwable::class.java))).thenReturn(0)
        }

        MockitoAnnotations.openMocks(this)
        homeRepository = HomeRepositoryImpl(firebaseAuth, firestore)
    }

    @Test
    fun invalid_post_on_empty_user_name() = runTest {
        homeRepository.addPost(Post("", "123", "Message1", "2025-01-01 18:00:00"))
            .collect{
                assertEquals("Failure", it.error)
            }
    }

    @Test
    fun invalid_post_on_empty_user_id() = runTest {
        homeRepository.addPost(Post("Klaseen", "", "Message1", "2025-01-01 18:00:00"))
            .collect{
                assertEquals("Failure", it.error)
            }
    }

    @Test
    fun invalid_post_on_empty_time() = runTest {
        homeRepository.addPost(Post("Klaseen", "123", "Message1", ""))
            .collect{
                assertEquals("Failure", it.error)
            }
    }


    @Test
    fun valid_post_returns_success_on_add_post_success() = runTest{
        whenever(firestore.collection("posts")).thenReturn(collection)
        whenever(collection.document()).thenReturn(document)
        whenever(document.set(Mockito.any())).thenReturn(Tasks.forResult(null))

        homeRepository.addPost(Post("Klaseen", "123", "Message1", "2025-01-01 18:00:00"))
            .collect{
                assertEquals("Success", it.data)
            }
    }

    @Test
    fun valid_post_returns_failure_on_add_post_failure() = runTest{
        whenever(firestore.collection("posts")).thenReturn(collection)
        whenever(collection.document()).thenReturn(document)
        whenever(document.set(Mockito.any())).thenReturn(Tasks.forException(Exception("Test Error")))

        homeRepository.addPost(Post("Klaseen", "123", "Message1", "2025-01-01 18:00:00"))
            .collect{
                assertEquals("Failure", it.error)
            }
    }
}