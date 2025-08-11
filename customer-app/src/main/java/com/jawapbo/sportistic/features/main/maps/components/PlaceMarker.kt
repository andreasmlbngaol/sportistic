package com.jawapbo.sportistic.features.main.maps.components

import android.util.Log
import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.jawapbo.sportistic.shared.data.merchants.Merchant

@Composable
fun PlaceMarker(
    merchant: Merchant,
    bookmarked: Boolean = false,
    onClick: () -> Unit
) {
    Marker(
        state = rememberUpdatedMarkerState(LatLng(merchant.latitude, merchant.longitude)),
        title = merchant.name,
        snippet = merchant.description,
        onClick = {
            onClick()
            Log.d("MapsScreen", "Marker clicked: ${merchant.id} (${merchant.name})")
            true
        },
        icon = BitmapDescriptorFactory.defaultMarker(
            if(!bookmarked) BitmapDescriptorFactory.HUE_ROSE else BitmapDescriptorFactory.HUE_YELLOW
        )
    )
}