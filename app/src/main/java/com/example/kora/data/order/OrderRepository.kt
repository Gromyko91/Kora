package com.example.kora.data.order

import android.util.Log
import com.example.kora.data.model.Address
import com.example.kora.data.model.Dish
import com.example.kora.data.model.Order
import com.example.kora.data.model.OrderItem
import com.example.kora.data.model.Restaurant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.UUID
import kotlin.random.Random

class OrderRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val TAG = "OrderRepository"

    suspend fun getRestaurantDetails(restaurantId: String): Restaurant? {
        return try {
            val snapshot = db.collection("restaurants").document(restaurantId).get().await()
            snapshot.toObject(Restaurant::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching restaurant", e)
            null
        }
    }

    suspend fun fetchUserOrders(userId: String) : List<Order> {
        return try {
            val snapshot = db.collection("orders")
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.toObjects(Order::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching user orders", e)
            emptyList()
        }
    }

    fun placeOrder(
        restaurantId: String,
        cartItems: Map<Dish, Int>,
        subtotal: Double,
        tax: Double,
        deliveryFee: Double,
        total: Double,
        paymentMethod: String,
        address: Address,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: return onError("User not logged in")
        val timestamp = System.currentTimeMillis()

        val orderId = "ORD-${Random.nextInt(100, 9999)}"

        val order = Order(
            id = orderId,
            restaurantId = restaurantId,
            userId = userId,
            status = "Pending",
            subtotal = subtotal,
            tax = tax,
            deliveryFee = deliveryFee,
            total = total,
            paymentStatus = "Unpaid",
            paymentMethod = paymentMethod,
            deliveryAddress = address,
            createdAt = timestamp,
            updatedAt = timestamp,
            updatedBy = ""
        )

        val batch = db.batch()

        val orderRef = db.collection("orders").document(orderId)
        batch.set(orderRef, order)

        cartItems.forEach { (dish, quantity) ->
            val itemId = UUID.randomUUID().toString()

            val itemRef = orderRef.collection("order-items").document(itemId)

            val orderItem = OrderItem(
                id = itemId,
                orderId = orderId,
                dishId = dish.id,
                name = dish.name,
                price = dish.price.replace(Regex("[^0-9.]"), "").toDoubleOrNull() ?: 0.0,
                quantity = quantity,
                imageUrl = dish.imageUrl
            )
            batch.set(itemRef, orderItem)
        }

        if (address.id.isEmpty()) {
            val addressId = "Addr-${Random.nextInt(1000, 9999)}"
            val newAddress = address.copy(id = addressId, userId = userId)
            val addressRef = db.collection("addresses").document(addressId)
            batch.set(addressRef, newAddress)
        }

        batch.commit()
            .addOnSuccessListener {
                Log.d(TAG, "Order placed successfully: $orderId")
                onSuccess(orderId)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to place order", e)
                onError(e.message ?: "Failed to place order")
            }
    }
}