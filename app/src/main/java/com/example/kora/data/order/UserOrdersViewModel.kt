package com.example.kora.data.order

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kora.data.model.Order
import com.example.kora.data.model.OrderItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class UserOrderUiState(
    val order: Order,
    val restaurantName: String,
    val itemCount: Int,
    val formattedDate: String
)

class UserOrdersViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val repository = OrderRepository()

    private val _uiState = MutableStateFlow<List<UserOrderUiState>>(emptyList())
    val uiState: StateFlow<List<UserOrderUiState>> = _uiState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _selectedOrderItems = MutableStateFlow<List<OrderItem>>(emptyList())
    val selectedOrderItems: StateFlow<List<OrderItem>> = _selectedOrderItems

    init {
        fetchMyOrders()
    }

    fun fetchMyOrders() {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val rawOrders = repository.fetchUserOrders(userId)
                val processedOrders = mutableListOf<UserOrderUiState>()

                val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())

                for (order in rawOrders) {
                    val restaurantName = try {
                        repository.getRestaurantDetails(order.restaurantId)?.name ?: "Unknown Restaurant"
                    } catch (e: Exception) { "Unknown Restaurant" }

                    val itemCount = try {
                        db.collection("orders")
                            .document(order.id)
                            .collection("order-items")
                            .get().await().size()
                    } catch (e: Exception) { 0 }

                    processedOrders.add(
                        UserOrderUiState(
                            order = order,
                            restaurantName = restaurantName,
                            itemCount = itemCount,
                            formattedDate = sdf.format(Date(order.createdAt))
                        )
                    )
                }
                _uiState.value = processedOrders
            } catch (e: Exception) {
                Log.e("UserOrdersVM", "Error loading orders", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchOrderItemsForSheet(orderId: String) {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("orders")
                    .document(orderId)
                    .collection("order-items")
                    .get()
                    .await()
                _selectedOrderItems.value = snapshot.toObjects(OrderItem::class.java)
            } catch (e: Exception) {
                Log.e("UserOrdersVM", "Error fetching items for sheet", e)
                _selectedOrderItems.value = emptyList()
            }
        }
    }

}