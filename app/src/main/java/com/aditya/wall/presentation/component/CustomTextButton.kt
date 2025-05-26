package com.aditya.wall.presentation.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextButton(
    title:String, color: Color, shape : RoundedCornerShape, isLoading:Boolean, modifier: Modifier, onClick: ()->Unit
) {
    TextButton(
        onClick = { onClick() },
        modifier = modifier,
        enabled = !isLoading,
        shape = shape,
        colors = ButtonDefaults
            .buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                contentColor = color,
                disabledContentColor = Color.LightGray
            )
    ) {
        Text(text = title, fontSize = 19.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.labelSmall)
    }
}