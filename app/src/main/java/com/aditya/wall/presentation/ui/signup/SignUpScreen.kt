package com.aditya.wall.presentation.ui.signup

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.navigation.NavController
import com.aditya.wall.presentation.component.CustomButton
import com.aditya.wall.presentation.navigation.Navigation
import com.aditya.wall.presentation.ui.home.HomeViewModel
import com.aditya.wall.utils.GoogleCredentialUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SignUpUi(
    navController: NavController,
    signUpViewModel: SignUpViewModel,
    homeViewModel: HomeViewModel,
    context: Context
){
    val credentialManager = CredentialManager.create(context)
    val signUpState = signUpViewModel.signUpState.collectAsState().value

    LaunchedEffect(signUpState) {
        if (signUpState.signInMessage == "Success"){
            navController.navigate(Navigation.Home.route){
                popUpTo(Navigation.SignUp.route){
                    inclusive = true
                }
            }
            Toast.makeText(context, "Logged In", Toast.LENGTH_SHORT).show()
            signUpViewModel.resetSignState()
            homeViewModel.getProfile()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomButton(
            title = "Sign In With Google",
            color = Color(0xFF00A3FF),
            shape = RoundedCornerShape(30.dp),
            isLoading = false,
            modifier = Modifier.height(54.dp),
            onClick = {
                CoroutineScope(
                    context = Main
                ).launch {
                    try {
                        val result = credentialManager.getCredential(
                            request = GoogleCredentialUtils(context).request,
                            context = context
                        )
                        signUpViewModel.onEvent(SignUpEvent.OnSignInButtonClick(result))
                    } catch (e: Exception) {
                        Log.w("SignUpUi", "CredentialManager.getCredential:failure", e)
                    }
                }
            }
        )
    }
}