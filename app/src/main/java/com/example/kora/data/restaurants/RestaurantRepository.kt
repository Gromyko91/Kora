package com.example.kora.data.restaurants

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.snapshotFlow
import com.example.kora.data.model.Restaurant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID

class RestaurantRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val TAG = "RestaurantRepo"

    fun addRestaurant(
        name: String,
        cuisine: String,
        deliveryFee: String,
        estTime: String,
        openingTime: String,
        closingTime: String,
        imageUri: Uri?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            onError("User not logged in")
            return
        }
        Log.d(TAG, "Starting add Restaurant process for $name")

        val restaurantId = UUID.randomUUID().toString()

        fun saveToFirestore(imageUrl: String) {
            val timestamp = System.currentTimeMillis()

            val restaurant = Restaurant(
                id = restaurantId,
                name = name,
                cuisine = cuisine,
                deliveryFee = deliveryFee,
                estTime = estTime,
                openingTime = openingTime,
                closingTime = closingTime,
                imageUrl = imageUrl,
                createdBy = currentUser.uid,
                createdAt = timestamp,
                updatedBy = currentUser.uid,
                updatedAt = timestamp
            )

            Log.d(TAG, "Saving data to firestore: $restaurant")

            db.collection("restaurants")
                .document(restaurantId)
                .set(restaurant)
                .addOnSuccessListener {
                    Log.d(TAG, "Restaurant saved successfully")
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Firestore Error: ${e.message}")
                    onError(e.message ?: "Failed to save restaurant details")
                }
        }

        if (imageUri != null) {
            Log.d(TAG, "Image detected. Uploading to Storage...")
            val imageRef = storage.reference.child("restaurant_images/$restaurantId.jpg")

            imageRef.putFile(imageUri)
                .addOnSuccessListener {
                    Log.d(TAG, "Image upload success. Fetching Download URL...")
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        Log.d(TAG, "Image URL retrieved: $uri")
                        saveToFirestore(uri.toString())
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Image upload failed: ${e.message}")
                    onError("Failed to upload image: ${e.message}")
                }
        } else {
            Log.w(TAG, "No image provided. Saving with empty URL.")
            saveToFirestore(" ")
        }
    }

    fun getRestaurants(): Flow<List<Restaurant>> = callbackFlow {
        val subscription = db.collection("restaurants")
            .orderBy("createdAt")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Listen failed", error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val restaurants = snapshot.toObjects(Restaurant::class.java)
                    trySend(restaurants)
                }
            }

        awaitClose { subscription.remove() }
    }
}