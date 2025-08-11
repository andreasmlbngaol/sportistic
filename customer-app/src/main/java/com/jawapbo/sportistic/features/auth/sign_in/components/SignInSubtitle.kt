package com.jawapbo.sportistic.features.auth.sign_in.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jawapbo.sportistic.core.component.CenterAlignedRow
import com.jawapbo.sportistic.ui.theme.VividOrange

@Composable
fun SignInSubtitle() {
    CenterAlignedRow {
        Text(
            text = "Sehat ",
            color = VividOrange,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp
        )

        Text(
            text = "dengan ",
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp
        )

        Text(
            text = "Cara",
            color = VividOrange,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp
        )

        Text(
            text = "mu",
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp
        )
    }
}