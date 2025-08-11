package com.jawapbo.sportistic.features.main.maps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.jawapbo.sportistic.core.component.BackButton
import com.jawapbo.sportistic.features.main.data.MapDefaults
import com.jawapbo.sportistic.features.main.data.MapDefaults.MAX_ZOOM
import com.jawapbo.sportistic.features.main.data.MapDefaults.MIN_ZOOM
import com.jawapbo.sportistic.features.main.maps.components.MapZoomSlider
import com.jawapbo.sportistic.features.main.maps.components.PlaceBottomSheet
import com.jawapbo.sportistic.features.main.maps.components.PlaceMarker
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapsScreen(
    onBack: () -> Unit,
    viewModel: MapsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(state.myPlace, 14.5f)
    }

    val zoomState by remember { derivedStateOf { cameraPositionState.position.zoom } }

    val defaultSliderState = (zoomState - MIN_ZOOM) / (MAX_ZOOM - MIN_ZOOM)
    val sliderState = rememberSliderState(defaultSliderState)
    sliderState.onValueChange = {
        val scaledZoom = it * (MAX_ZOOM - MIN_ZOOM) + MIN_ZOOM
        cameraPositionState.move(CameraUpdateFactory.zoomTo(scaledZoom))
    }

    LaunchedEffect(zoomState) {
        val sliderValue = (zoomState - MIN_ZOOM) / (MAX_ZOOM - MIN_ZOOM)
        sliderState.value = sliderValue.coerceIn(0f, 1f)
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapDefaults.PROPERTIES.value,
                uiSettings = MapDefaults.UI_SETTINGS.value,
                mapColorScheme = ComposeMapColorScheme.FOLLOW_SYSTEM,
            ) {
                state.merchants.forEach { merchant ->
                    PlaceMarker(
                        merchant = merchant,
                        bookmarked = state.bookmarkedMerchants.contains(merchant.id)
                    ) { viewModel.onEvent(MapsEvent.OnMerchantSelected(merchant)) }
                }
            }

            BackButton(
                onBack = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
            )

            MapZoomSlider(
                visible = state.zoomSliderVisible,
                sliderState = sliderState,
                onToggleZoomSlider = { viewModel.onEvent(MapsEvent.OnToggleZoomSlider) }
            )

            state.selectedMerchant?.let { merchant ->
                PlaceBottomSheet(
                    merchant = merchant,
                    bookmarkStatus = state.bookmarkedMerchants.contains(merchant.id),
                    onDismissRequest = { viewModel.onEvent(MapsEvent.OnMerchantDeselected) },
                    onNavigate = { latLng, name ->
                        viewModel.onEvent(MapsEvent.OnOpenGoogleMaps(latLng, name))
                    },
                    onAddToBookmark = { placeId ->
                        viewModel.onEvent(MapsEvent.OnAddToBookmark(placeId))
                    },
                    onRemoveFromBookmark = { placeId ->
                        viewModel.onEvent(MapsEvent.OnRemoveFromBookmark(placeId))
                    }
                )
            }
        }
    }
}