package com.aditya.wall.presentation.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aditya.wall.presentation.ui.home.HomeUi
import com.aditya.wall.presentation.ui.home.HomeViewModel
import com.aditya.wall.presentation.ui.signup.SignUpUi
import com.aditya.wall.presentation.ui.signup.SignUpViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainNavigation(
    navHostController: NavHostController,
    homeViewModel: HomeViewModel,
    signUpViewModel: SignUpViewModel,
    context: Context,
    modifier: Modifier
) {

    var startDestination = Navigation.SignUp.route

    FirebaseAuth.getInstance().currentUser?.let {
        startDestination = Navigation.Home.route
    }

    NavHost(navController = navHostController, startDestination = startDestination) {

        composable(Navigation.SignUp.route) {
            SignUpUi(navHostController, signUpViewModel, homeViewModel, context)
        }

        composable(Navigation.Home.route) {
            HomeUi(navHostController, homeViewModel, context)
        }
    }
}