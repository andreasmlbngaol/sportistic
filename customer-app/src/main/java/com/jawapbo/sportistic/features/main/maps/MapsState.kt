package com.jawapbo.sportistic.features.main.maps

import com.google.android.gms.maps.model.LatLng
import com.jawapbo.sportistic.shared.data.merchants.Merchant

data class MapsState(
    val merchants: List<Merchant> = emptyList(),
    val myPlace: LatLng = LatLng(3.571975560442963, 98.65209651509176),
    val bookmarkedMerchants: List<Long> = emptyList(),
    val selectedMerchant: Merchant? = null,
    val zoomSliderVisible: Boolean = false
)