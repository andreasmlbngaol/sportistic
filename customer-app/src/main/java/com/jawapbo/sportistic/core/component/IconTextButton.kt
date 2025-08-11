package com.jawapbo.sportistic.core.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IconTextButton(
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors()
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shapes = ButtonDefaults.shapes(),
        colors = colors
    ) {

        icon()
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(text = label)
    }

}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IconTextButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors()
) {

    IconTextButton(
        onClick = onClick,
        icon = {
            Icon(
                imageVector = imageVector,
                contentDescription = label
            )
        },
        label = label,
        modifier = modifier,
        colors = colors
    )
}