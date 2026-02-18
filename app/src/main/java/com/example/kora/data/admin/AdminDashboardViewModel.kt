package com.example.kora.data.admin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kora.data.model.Order
import com.example.kora.data.model.Restaurant
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AdminDashboardViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val  _pendingOrdersCount = MutableStateFlow(0)
    val pendingOrdersCount: StateFlow<Int> = _pendingOrdersCount

    private val _totalRevenue = MutableStateFlow(0.0)
    val totalRevenue: StateFlow<Double> = _totalRevenue

    private val _activities = MutableStateFlow<List<ActivityItem>>(emptyList())
    val activites: StateFlow<List<ActivityItem>> = _activities

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchDashboardData()
    }

    fun fetchDashboardData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val orderSnapshot = db.collection("orders").get().await()
                val orders = orderSnapshot.toObjects(Order::class.java)

                val pending = orders.count { it.status.equals("Pending", ignoreCase = true) }
                val revenue = orders
                    .filter { it.status.equals("Delivered", ignoreCase = true) || it.status.equals("Completed", ignoreCase = true) }
                    .sumOf { it.total }

                _pendingOrdersCount.value = pending
                _totalRevenue.value = revenue

                val restaurantSnapshot = db.collection("restaurants").get().await()
                val restaurants = restaurantSnapshot.toObjects(Restaurant::class.java)

                val orderActivities = orders.map { order ->
                    ActivityItem(
                        id = order.id,
                        title = "New Order #${order.id.takeLast(4)}",
                        description = "${fetchUserName(order.userId)} placed an order",
                        timestamp = order.createdAt,
                        type = ActivityType.ORDER,
                        amount = order.total
                    )
                }

                val restaurantActivities = restaurants.map { restaurant ->
                    ActivityItem(
                        id = restaurant.id,
                        title = "New Restaurant",
                        description = "${restaurant.name} was added",
                        timestamp = restaurant.createdAt,
                        type = ActivityType.RESTAURANT
                    )
                }

                val allActivities = (orderActivities + restaurantActivities)
                    .sortedByDescending { it.timestamp }

                _activities.value = allActivities
            } catch (e: Exception) {
                Log.e("AdminDashVM", "Error loading dashboard", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun fetchUserName(userId: String): String {
        return try {
            val doc = db.collection("users").document(userId).get().await()
            val first = doc.getString("firstName") ?: ""
            val last = doc.getString("lastName") ?: ""
            if (first.isBlank()) " Customer" else "$first $last"
        } catch (e: Exception) { "Customer"}
    }


}