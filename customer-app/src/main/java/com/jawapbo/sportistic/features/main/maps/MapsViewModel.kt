package com.jawapbo.sportistic.features.main.maps

import android.Manifest
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.jawapbo.sportistic.shared.data.core.SportisticRepository
import com.jawapbo.sportistic.shared.data.merchants.Merchant
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MapsViewModel(
    private val repository: SportisticRepository,
    private val context: Application
): ViewModel() {
    private val _state = MutableStateFlow(MapsState())
    val state = _state
        .onStart {
            getMerchants()
            getMyLocation()
            getBookmarkedMerchants()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _state.value
        )

    fun onEvent(event: MapsEvent) {
        when (event) {
            is MapsEvent.OnAddToBookmark -> onAddToBookmark(event.merchantId)
            is MapsEvent.OnRemoveFromBookmark -> onRemoveFromBookmark(event.merchantId)
            is MapsEvent.OnMerchantSelected -> onMerchantSelected(event.merchant)
            is MapsEvent.OnMerchantDeselected -> onMerchantDeselected()
            is MapsEvent.OnOpenGoogleMaps -> onOpenGoogleMaps(event.location, event.name)
            is MapsEvent.OnToggleZoomSlider -> toggleZoomSlider()
        }
    }

    private fun toggleZoomSlider() {
        _state.update { it.copy(zoomSliderVisible = !_state.value.zoomSliderVisible) }
    }

    private fun onMerchantSelected(merchant: Merchant) {
        _state.update { it.copy(selectedMerchant = merchant) }
    }

    private fun onMerchantDeselected() {
        _state.update { it.copy(selectedMerchant = null) }
    }

    private fun onOpenGoogleMaps(location: LatLng, name: String) {
        val lat = location.latitude
        val lng = location.longitude

        val uri = "geo:${lat},${lng}?q=$lat,$lng($name)".toUri()
        val mapIntent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.google.android.apps.maps")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        if(mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        }
    }

    private fun onAddToBookmark(merchantId: Long) {
        viewModelScope.launch {
            repository.addMerchantToBookmark(merchantId)?.let {
                _state.update { state ->
                    state.copy(bookmarkedMerchants = it)
                }
            }
        }
    }

    private fun onRemoveFromBookmark(merchantId: Long) {
        viewModelScope.launch {
            repository.removeMerchantFromBookmark(merchantId)?.let {
                _state.update { state ->
                    state.copy(bookmarkedMerchants = it)
                }
            }
        }
    }

    private suspend fun getMerchants() {
        repository.getMerchants()?.let {
            _state.update { state ->
                state.copy(merchants = it)
            }
        }
    }

    private suspend fun getBookmarkedMerchants() {
        repository.getBookmarkedMerchants()?.let {
            _state.update { state ->
                state.copy(bookmarkedMerchants = it)
            }
        }
    }

    private fun getMyLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val locationPermissionGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (locationPermissionGranted) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    Log.d("MapsScreen", "User location: ${it.latitude}, ${it.longitude}")
                    _state.update { state ->
                        state.copy(myPlace = LatLng(it.latitude, it.longitude))
                    }
                }
            }
        }
    }
}