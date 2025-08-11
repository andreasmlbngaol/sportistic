package com.jawapbo.sportistic.features.main.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings

object MapDefaults {
    const val MIN_ZOOM = 13f
    const val MAX_ZOOM = 21.5f

    val UI_SETTINGS
        @Composable
        get() = remember {
            mutableStateOf(MapUiSettings(
                compassEnabled = false,
                indoorLevelPickerEnabled = false,
                mapToolbarEnabled = false,
                myLocationButtonEnabled = true,
                zoomControlsEnabled = false
            ))
        }

    val PROPERTIES
        @Composable
        get() = remember {
            mutableStateOf(MapProperties(
                isBuildingEnabled = true,
                isIndoorEnabled = true,
                isMyLocationEnabled = true,
                isTrafficEnabled = true,
                mapType = MapType.NORMAL,
                minZoomPreference = 13.5f,
            ))
        }
}