package com.jawapbo.sportistic.features.main.maps

import com.google.android.gms.maps.model.LatLng
import com.jawapbo.sportistic.shared.data.merchants.Merchant

sealed class MapsEvent {
    data object OnToggleZoomSlider: MapsEvent()
    data class OnMerchantSelected(val merchant: Merchant): MapsEvent()
    data object OnMerchantDeselected: MapsEvent()
    data class OnOpenGoogleMaps(val location: LatLng, val name: String): MapsEvent()
    data class OnAddToBookmark(val merchantId: Long): MapsEvent()
    data class OnRemoveFromBookmark(val merchantId: Long): MapsEvent()

}