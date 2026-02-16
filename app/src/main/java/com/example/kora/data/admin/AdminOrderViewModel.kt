package com.example.kora.data.admin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kora.data.model.Order
import com.example.kora.data.model.OrderItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class AdminOrderUiState(
    val order: Order,
    val customerName: String,
    val restaurantName: String,
    val itemCount: Int,
    val formattedTime: String
)

class AdminOrderViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    
    private val _orders = MutableStateFlow<List<AdminOrderUiState>>(emptyList())
    val orders: StateFlow<List<AdminOrderUiState>> = _orders

    private val _selectedOrderItems = MutableStateFlow<List<OrderItem>>(emptyList())
    val selectedOrderItems: StateFlow<List<OrderItem>> = _selectedOrderItems

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchAllOrders()
    }

    fun fetchAllOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val snapshot = db.collection("orders")
                    .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
                    .get()
                    .await()

                val rawOrders = snapshot.toObjects(Order::class.java)
                val uiOrders = mutableListOf<AdminOrderUiState>()

                for (order in rawOrders) {
                    val customerName = fetchUserName(order.userId)
                    val restaurantName = fetchRestaurantName(order.restaurantId)
                    val itemCount = fetchItemCount(order.id)

                    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
                    val time = sdf.format(Date(order.createdAt))

                    uiOrders.add(
                        AdminOrderUiState(
                            order = order,
                            customerName = customerName,
                            restaurantName = restaurantName,
                            itemCount = itemCount,
                            formattedTime = time
                        )
                    )
                }
                _orders.value = uiOrders
            } catch (e: Exception) {
                Log.e("AdminVM", "Error fetching orders", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun fetchUserName(userId: String): String {
        return try {
            val doc = db.collection("users").document(userId).get().await()
            val firstName = doc.getString("firstName") ?: ""
            val lastName = doc.getString("lastName") ?: ""

            if (firstName.isEmpty() && lastName.isEmpty()) {
                "Unknown Customer"
            } else {
                "$firstName $lastName".trim()
            }
        } catch (e: Exception) {
            "unknown Customer"
        }
    }

    private suspend fun fetchRestaurantName(restaurantId: String): String {
        return try {
            val doc = db.collection("restaurants").document(restaurantId).get().await()
            doc.getString("name") ?: "Unknown Restaurant"
        } catch (e: Exception) {"Unknown Restaurant"}
    }

    private suspend fun fetchItemCount(orderId: String): Int {
        return try {
            val snapshot = db.collection("orders")
                .document(orderId)
                .collection("order-items")
                .get()
                .await()
            snapshot.size()
        } catch (e: Exception) {0}
    }

    fun fetchOrderItems(orderId: String) {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("orders")
                    .document(orderId)
                    .collection("order-items")
                    .get()
                    .await()
                _selectedOrderItems.value = snapshot.toObjects(OrderItem::class.java)
            } catch (e: Exception) {
                Log.e("AdminVm", "Error items", e)
                _selectedOrderItems.value = emptyList()
            }
        }
    }

    fun updateStatus(orderId: String, newStatus: String) {
        viewModelScope.launch {
            try {
                db.collection("orders").document(orderId)
                    .update("status", newStatus)
                    .await()

                fetchAllOrders()
            } catch (e: Exception) {
                Log.e("AdminVM", "Failed to update Status", e)
            }
        }
    }
}






