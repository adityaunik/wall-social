package com.aditya.wall.presentation.ui.home

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aditya.wall.domain.model.Profile
import com.aditya.wall.presentation.component.CustomButton
import com.aditya.wall.presentation.component.CustomTextField
import com.aditya.wall.domain.model.Post
import com.aditya.wall.presentation.component.Loader
import com.aditya.wall.presentation.component.Post
import com.aditya.wall.presentation.navigation.Navigation
import com.aditya.wall.utils.DateTimeUtils
import java.time.LocalDateTime
import kotlin.math.truncate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeUi(
    navController: NavController,
    homeViewModel: HomeViewModel,
    context: Context
){
    val focusManager = LocalFocusManager.current

    val homeState = homeViewModel.homeState.collectAsState().value
    val profile = homeViewModel.profile.collectAsState().value

    LaunchedEffect(Unit) {
        homeViewModel.fetchPosts()
    }

    LaunchedEffect(homeState) {
        if (homeState.addPostMessage == "Success"){
            Toast.makeText(context, "Post added", Toast.LENGTH_SHORT).show()
            homeViewModel.resetPostContent()
        }

        if (homeState.logOutMessage == "Success"){
            Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
            navController.navigate(Navigation.SignUp.route){
                popUpTo(Navigation.Home.route){
                    inclusive = true
                }
            }
            homeViewModel.resetHomeState()
        }
    }

    Column(
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ){
                focusManager.clearFocus()
            }
    ) {
        CustomTextField(homeState.content) {
            homeViewModel.onEvent(HomeEvent.OnContentValueChange(it))
        }
        Spacer(Modifier.height(12.dp))
        CustomButton(
            title = "Add to the wall",
            color = Color(0xFF00A3FF),
            shape = RoundedCornerShape(12.dp),
            isLoading = homeState.isPosting,
            modifier = Modifier.height(48.dp)
                .padding(horizontal = 12.dp),
            onClick = {
                if (homeState.content != ""){
                    focusManager.clearFocus()
                    homeViewModel.onEvent(HomeEvent.OnAddToWallButtonClick(
                        Post(
                            userName = profile.userName,
                            userId = profile.userId,
                            content = homeState.content,
                            time = DateTimeUtils.formatter.format(LocalDateTime.now())
                        )
                    ))
                }
                else {
                    Toast.makeText(context, "Share something", Toast.LENGTH_SHORT).show()
                }
            }
        )
        Spacer(Modifier.height(15.dp))
        Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(color = Color.Black.copy(0.1f)))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(text = "Show My Posts", color = Color.Black, modifier = Modifier.align(Alignment.CenterVertically))
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = homeState.switchState,
                onCheckedChange = {
                    focusManager.clearFocus()
                    homeViewModel.onEvent(HomeEvent.OnTogglePost(it, profile.userId))
                },
                modifier = Modifier.align(Alignment.CenterVertically),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF00A3FF),
                    uncheckedThumbColor = Color(0xFFBFBFBF),
                    uncheckedTrackColor = Color.Transparent,
                    disabledCheckedThumbColor = Color(0xFFBFBFBF).copy(alpha = 0.5f),
                    disabledCheckedTrackColor = Color(0xFFBFBFBF).copy(alpha = 0.5f),
                    disabledUncheckedThumbColor = Color(0xFFBFBFBF).copy(alpha = 0.5f),
                    disabledUncheckedTrackColor = Color(0xFFBFBFBF).copy(alpha = 0.5f)
                )
            )
        }

        if (homeState.postLoading){
            Loader()
        }
        else{
            LazyColumn {
                itemsIndexed(homeState.posts) { _, post ->
                    Post(post)
                }
            }
        }
    }
}