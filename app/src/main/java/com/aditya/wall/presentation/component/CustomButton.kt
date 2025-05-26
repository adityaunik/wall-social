package com.aditya.wall.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomButton(title:String, color:Color, shape : RoundedCornerShape, isLoading:Boolean, modifier: Modifier, onClick: ()->Unit) {
    Button(
        onClick = { onClick() },
        modifier = modifier,
        shape = shape,
        colors = ButtonDefaults
            .buttonColors(
                containerColor = color,
                disabledContainerColor = Color.LightGray,
                contentColor = Color.White,
                disabledContentColor = Color.Black
            )
    ) {
        Box(
        ){
            if (isLoading) {
                ButtonLoader()
            }
                Text(
                    text = title,
                    color = if (isLoading) Color.White.copy(alpha = 0f) else Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.labelSmall
                )


        }
    }
}