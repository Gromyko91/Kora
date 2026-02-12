package com.example.kora.data.dishes

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kora.data.model.Dish
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DishViewModel : ViewModel() {
    private val repository = DishRepository()
    private val TAG = "DishViewModel"

    private val _addDishState = MutableStateFlow<UiState>(UiState.Idle)
    val addDishState: StateFlow<UiState> = _addDishState

    private val _dishes = MutableStateFlow<List<Dish>>(emptyList())
    val dishes: StateFlow<List<Dish>> = _dishes

    fun saveDish(
        restaurantId: String,
        name: String,
        category: String,
        price: String,
        description: String,
        prepTime: String,
        allergens: List<String>,
        customization: String,
        isAvailable: Boolean,
        imageUri: Uri?
    ) {
        Log.d(TAG, "SaveDish called. name: $name, price: $price")
        _addDishState.value = UiState.Loading

        repository.addDish(
            restaurantId, name, category, price, description, prepTime, allergens, customization, isAvailable, imageUri,
            onSuccess = {
                Log.d(TAG, "saveDish success callback received in ViewModel")
                _addDishState.value = UiState.Success
            },
            onError = { error ->
                Log.e(TAG, "saveDish error callback: $error")
                _addDishState.value = UiState.Error(error)
            }
        )
    }
    fun fetchDishes(restaurantId: String) {
        Log.d(TAG, "fetchDishes called for ID: $restaurantId")
        viewModelScope.launch {
            repository.getDishes(restaurantId).collect { list ->
                Log.d(TAG, "Collected ${list.size} dishes from repository")
                _dishes.value = list
            }
        }
    }

    fun resetAddState() {
        Log.d(TAG, "Resetting add state")
        _addDishState.value = UiState.Idle
    }

    fun toggleAvailability(dishId: String, currentStatus: Boolean) {
        repository.updateDishAvailability(dishId, !currentStatus, {}, {})
    }

    fun deleteDish(dishId: String, restaurantId: String, imageUrl: String) {
        repository.deleteDish(dishId, restaurantId, imageUrl, {}, {})
    }
}

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    object Success : UiState()
    data class Error(val message: String) : UiState()
}