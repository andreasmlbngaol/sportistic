package com.jawapbo.sportistic.features.auth.sign_in.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jawapbo.sportistic.core.component.OutlinedText
import com.jawapbo.sportistic.core.component.CenterAlignedRow
import com.jawapbo.sportistic.ui.theme.VividOrange

@Composable
fun SignInTitle() {
    CenterAlignedRow {
        OutlinedText(
            text = "Go",
            textColor = Color.White,
            outlineColor = VividOrange
        )
        OutlinedText(
            text = "Sehat",
            textColor = VividOrange,
            outlineColor = Color.White
        )
    }
}