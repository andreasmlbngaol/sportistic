package com.jawapbo.sportistic.core.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun OutlinedText(
    text: String,
    textColor: Color,
    outlineColor: Color,
    fontSize: TextUnit = 36.sp,
    fontWeight: FontWeight = FontWeight.Bold,
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = fontWeight,
            style = TextStyle(
                drawStyle = Stroke(width = 3f),
                color = outlineColor
            )
        )

        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = textColor,
        )
    }
}