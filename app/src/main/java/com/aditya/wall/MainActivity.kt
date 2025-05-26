package com.aditya.wall

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aditya.wall.presentation.component.CustomTextButton
import com.aditya.wall.presentation.navigation.MainNavigation
import com.aditya.wall.presentation.navigation.Navigation
import com.aditya.wall.presentation.ui.home.HomeEvent
import com.aditya.wall.presentation.ui.home.HomeViewModel
import com.aditya.wall.presentation.ui.signup.SignUpViewModel
import com.aditya.wall.ui.theme.WallTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WallTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                val homeViewModel = hiltViewModel<HomeViewModel>()
                val signUpViewModel = hiltViewModel<SignUpViewModel>()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        Surface(
                            shadowElevation = 2.dp
                        ) {
                            CenterAlignedTopAppBar(
                                title = { Text(text = "Wall", fontSize = 24.sp, fontWeight = FontWeight.Bold) },
                                navigationIcon = {
                                    if (navBackStackEntry?.destination?.route == Navigation.Home.route) {
                                        CustomTextButton(
                                            title = "Log Out",
                                            color = Color(0xFF00A3FF),
                                            shape = RoundedCornerShape(12.dp),
                                            isLoading = false,
                                            modifier = Modifier.height(48.dp),
                                        ) {
                                            homeViewModel.onEvent(HomeEvent.OnLogOutButtonClick)
                                        }
                                    }
                                },
                                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                    containerColor = Color.White,
                                    titleContentColor = Color.Black
                                )
                            )
                        }
                    }
                ) { innerPadding ->

                    val context = LocalContext.current
                    val systemUiController = rememberSystemUiController()

                    systemUiController.setSystemBarsColor(
                        color = Color.White,
                        darkIcons = true
                    )

                    Column(
                        modifier = Modifier.fillMaxSize()
                            .padding(innerPadding)
                            .background(color = Color.White)
                    ) {
                        MainNavigation(navController, homeViewModel, signUpViewModel, context, modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}
