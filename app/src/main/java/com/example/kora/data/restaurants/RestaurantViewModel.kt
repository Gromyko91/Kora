package com.example.kora.data.restaurants

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kora.data.model.Restaurant
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RestaurantViewModel : ViewModel() {
    private val repository = RestaurantRepository()

    private val _uiState = MutableStateFlow<RestaurantUiState>(RestaurantUiState.Idle)
    val uiState: StateFlow<RestaurantUiState> = _uiState

    fun saveRestaurant(
        name: String,
        cuisine: String,
        deliveryFee: String,
        estTime: String,
        openingTime: String,
        closingTime: String,
        imageUri: Uri?
    ) {
        _uiState.value = RestaurantUiState.Loading

        repository.addRestaurant(
            name, cuisine, deliveryFee, estTime, openingTime, closingTime, imageUri,
            onSuccess = {
                _uiState.value = RestaurantUiState.Success
            },
            onError = { error ->
                _uiState.value = RestaurantUiState.Error(error)
            }
        )
    }

    fun resetState() {
        _uiState.value = RestaurantUiState.Idle
    }

    private val _restaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val restaurants: StateFlow<List<Restaurant>> = _restaurants

    init {
        fetchRestaurants()
    }

    private fun fetchRestaurants() {
        viewModelScope.launch {
            repository.getRestaurants().collect { list ->
                _restaurants.value = list
            }
        }
    }

    private val _selectedRestaurant = MutableStateFlow<Restaurant?>(null)
    val selectedRestaurant: StateFlow<Restaurant?> = _selectedRestaurant

    fun selectedRestaurant(restaurant: Restaurant) {
        _selectedRestaurant.value = restaurant
    }

    fun deleteRestaurant(restaurant: Restaurant) {
        repository.deleteRestaurant(restaurant.id, restaurant.imageUrl,
            onSuccess = { },
            onError = { }
        )
    }

    fun updateRestaurantDetails(
        id: String,
        name: String,
        cuisine: String,
        deliveryFee: String,
        estTime: String,
        openingTime: String,
        closingTime: String,
        imageUri: Uri?
    ) {
        _uiState.value = RestaurantUiState.Loading

        val updates = mutableMapOf<String, Any>(
            "name" to name,
            "cuisine" to cuisine,
            "deliveryFee" to deliveryFee,
            "estTime" to estTime,
            "openingTime" to openingTime,
            "closingTime" to closingTime,
        )

        if (imageUri != null) {
            val storageRef = FirebaseStorage.getInstance()
                .reference.child("restaurant_images/$id.jpg")

            storageRef.putFile(imageUri)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        updates["imageUrl"] = downloadUrl.toString()

                        performUpdate(id, updates)
                    }
                }
                .addOnFailureListener { e ->
                    _uiState.value = RestaurantUiState.Error("Image upload failed: ${e.message}")
                }
        } else {
            performUpdate(id, updates)
        }

    }

    private fun performUpdate(id: String, updates: Map<String, Any>) {
        repository.updateRestaurant(id, updates,
            onSuccess = {
                _uiState.value = RestaurantUiState.Success
            },
            onError = { error ->
                _uiState.value = RestaurantUiState.Error(error)
            }
            )
    }
}

sealed class RestaurantUiState {
    object Idle : RestaurantUiState()
    object Loading : RestaurantUiState()
    object Success : RestaurantUiState()
    data class Error(val message: String) : RestaurantUiState()
}