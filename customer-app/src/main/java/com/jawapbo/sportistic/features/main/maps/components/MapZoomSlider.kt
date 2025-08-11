package com.jawapbo.sportistic.features.main.maps.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.material3.VerticalSlider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BoxScope.MapZoomSlider(
    visible: Boolean,
    sliderState: SliderState,
    onToggleZoomSlider: () -> Unit,
) {
    Column(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(16.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .heightIn(max = 250.dp)
    ) {
        FilledIconButton(
            onClick = onToggleZoomSlider
        ) {
            Icon(
                imageVector = if(visible) Icons.Filled.ZoomOut else Icons.Filled.ZoomIn,
                contentDescription = "Zoom"
            )
        }

        Spacer(Modifier.size(2.dp))

        AnimatedVisibility(
            visible = visible,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            VerticalSlider(
                state = sliderState,
                reverseDirection = true,
                track = {
                    SliderDefaults.Track(
                        sliderState = sliderState,
                        modifier = Modifier
                            .width(32.dp)
                    )
                }
            )
        }
    }
}