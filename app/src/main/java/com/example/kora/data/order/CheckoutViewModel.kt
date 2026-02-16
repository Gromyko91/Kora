package com.example.kora.data.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kora.data.model.Address
import com.example.kora.data.model.Dish
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class CheckoutUiState {
    object Idle : CheckoutUiState()
    object Loading : CheckoutUiState()
    data class Success(val orderId: String) : CheckoutUiState()
    data class Error(val message: String) : CheckoutUiState()
}

class CheckoutViewModel : ViewModel() {
    private val repository = OrderRepository()

    private val _uiState = MutableStateFlow<CheckoutUiState>(CheckoutUiState.Idle)
    val uiState: StateFlow<CheckoutUiState> = _uiState

    private val _deliveryFee = MutableStateFlow(0.0)
    val deliveryFee: StateFlow<Double> = _deliveryFee

    private val _estTime = MutableStateFlow("40-45")
    val estTime: StateFlow<String> = _estTime.asStateFlow()

    private val _isFetchingFee = MutableStateFlow(false)
    val isFetchingFee: StateFlow<Boolean> = _isFetchingFee

    fun fetchDeliveryFee(restaurantId: String) {
        if (restaurantId.isEmpty()) return

        viewModelScope.launch {
            _isFetchingFee.value = true
            val restaurant = repository.getRestaurantDetails(restaurantId)
            if (restaurant != null) {
                val feeString = restaurant.deliveryFee
                val fee = if (feeString.equals("Free", ignoreCase = true)) {
                    0.0
                } else {
                    feeString.replace(Regex("[^0-9.]"), "").toDoubleOrNull() ?: 0.0
                }
                _deliveryFee.value = fee

                if (restaurant.estTime.isNotEmpty()) {
                    _estTime.value = restaurant.estTime
                }
            }
            _isFetchingFee.value = false
        }
    }

    fun submitOrder(
        cartItems: Map<Dish, Int>,
        subtotal: Double,
        tax: Double,
        total: Double,
        paymentMethod: String,
        address: Address
        ) {
        if (cartItems.isEmpty()) {
            _uiState.value = CheckoutUiState.Error("Cart is empty")
            return
        }

        val restaurantId = cartItems.keys.firstOrNull()?.restaurantId ?: ""
        val currentDeliveryFee = _deliveryFee.value

        _uiState.value = CheckoutUiState.Loading

        repository.placeOrder(
            restaurantId = restaurantId,
            cartItems = cartItems,
            subtotal = subtotal,
            tax = tax,
            deliveryFee = currentDeliveryFee,
            total = total,
            paymentMethod = paymentMethod,
            address = address,
            onSuccess = { orderId ->
                _uiState.value = CheckoutUiState.Success(orderId)
            },
            onError = { error ->
                _uiState.value = CheckoutUiState.Error(error)
            }
        )
    }

    fun resetState() {
        _uiState.value = CheckoutUiState.Idle
    }
}