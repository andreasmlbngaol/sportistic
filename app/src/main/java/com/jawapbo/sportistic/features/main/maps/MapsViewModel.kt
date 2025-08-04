package com.jawapbo.sportistic.features.main.maps

import androidx.lifecycle.ViewModel
import com.jawapbo.sportistic.features.main.data.Place
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Suppress("unused")
class MapsViewModel(
    private val httpClient: HttpClient
): ViewModel() {
    private val _places = MutableStateFlow<List<Place>>(emptyList())
    val places = _places.asStateFlow()

    suspend fun getPlaces() {
        val response = httpClient.get("https://example.com/places")
        val places = response.body<List<Place>>()
        _places.value = places

    }

}