package com.aditya.wall.presentation.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ButtonLoader(
    isLoading: Boolean = true,
    title: String = "Hang On..."
){
    val infiniteTransition  = rememberInfiniteTransition(label = "")
    val offsetY = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                100f at 0 using LinearEasing
                (0f) at 500 using  LinearEasing
                (100f) at 1000 using  LinearEasing
                0f at 1500 using  LinearEasing
            }
        ), label = ""
    )

    Box(modifier = Modifier
        .background(color = Color.Transparent),
        contentAlignment = Alignment.Center){

        Spacer(modifier = Modifier
            .offset(x = offsetY.value.dp)
            .size(28.dp)
            .background(color = Color.White, shape = CircleShape))


    }
}