package com.aditya.wall.utils

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.getString
import androidx.credentials.GetCredentialRequest
import com.aditya.wall.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleCredentialUtils @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val googleIdOption = GetGoogleIdOption.Builder()
        .setServerClientId(getString(context, R.string.web_client_id))
        .setFilterByAuthorizedAccounts(false)
        .build()


    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()
}