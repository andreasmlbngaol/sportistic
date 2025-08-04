package com.jawapbo.sportistic.features.main.maps

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalSlider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.jawapbo.sportistic.features.main.data.PLACES
import com.jawapbo.sportistic.features.main.data.PlaceMarker
import org.koin.androidx.compose.koinViewModel

@Suppress("unused")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MapsScreen(
    onBack: () -> Unit,
    viewModel: MapsViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var userLocation by remember { mutableStateOf(LatLng(3.571975560442963, 98.65209651509176)) }

    var markerClicked by remember { mutableStateOf<PlaceMarker?>(null) }
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    Log.d("MapsScreen", "User location: ${it.latitude}, ${it.longitude}")
                    userLocation = LatLng(it.latitude, it.longitude)
                }
            }
        }
    }

    val places = remember {
        PLACES
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation, 15f)
    }

    val zoomMin = 13f
    val zoomMax = 22f

    val zoomState by remember {
        derivedStateOf { cameraPositionState.position.zoom }
    }
    val sliderState = rememberSliderState(
        value = (zoomState - zoomMin) / (zoomMax - zoomMin),
    )

    sliderState.onValueChange = {
        val newZoom = it * (zoomMax - zoomMin) + zoomMin
        cameraPositionState.move(CameraUpdateFactory.zoomTo(newZoom))
    }

    LaunchedEffect(zoomState) {
        val sliderValue = (zoomState - zoomMin) / (zoomMax - zoomMin)
        sliderState.value = sliderValue.coerceIn(0f, 1f)
    }

    var zoomSlideVisible by remember { mutableStateOf(false) }

    var uiSettings by remember {
        mutableStateOf(MapUiSettings(
            compassEnabled = false,
            indoorLevelPickerEnabled = false,
            mapToolbarEnabled = false,
            myLocationButtonEnabled = true,
            zoomControlsEnabled = false
        ))
    }
    var properties by remember {
        mutableStateOf(MapProperties(
            isBuildingEnabled = true,
            isIndoorEnabled = true,
            isMyLocationEnabled = true,
            isTrafficEnabled = true,
            mapType = MapType.NORMAL,
            minZoomPreference = 13.5f,
        ))
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
                properties = properties,
                uiSettings = uiSettings,
                mapColorScheme = ComposeMapColorScheme.FOLLOW_SYSTEM,
                onPOIClick = {
                    val name = it.name
                    val lat = it.latLng.latitude
                    val lng = it.latLng.longitude

                    Log.d("MapsScreen", "onMapClick: $name ($lat, $lng)")
                },
            ) {
                places.forEach { place ->
                    Marker(
                        state = rememberUpdatedMarkerState(LatLng(place.lat, place.lng)),
                        title = place.name,
                        snippet = place.description,
                        onClick = {
                            markerClicked = place
                            Log.d("MapsScreen", "Marker clicked: ${place.id} (${place.name})")
                            true
                        },
                        icon = BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_VIOLET
                        )
                    )
                }
            }

            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .heightIn(max = 200.dp)
            ) {
                FilledIconButton(
                    onClick = { zoomSlideVisible = !zoomSlideVisible }
                ) {
                    Icon(
                        imageVector = if(zoomSlideVisible) Icons.Filled.ZoomOut else Icons.Filled.ZoomIn,
                        contentDescription = "Zoom"
                    )
                }

                Spacer(Modifier.size(2.dp))

                AnimatedVisibility(
                    visible = zoomSlideVisible,
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

            markerClicked?.let { marker ->
                ModalBottomSheet(
                    onDismissRequest = { markerClicked = null },
                    sheetState = sheetState
                ) {
                    Column {
                        Text(
                            text = marker.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = marker.description ?: "No Description",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}