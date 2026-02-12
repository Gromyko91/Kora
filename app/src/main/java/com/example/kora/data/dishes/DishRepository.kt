package com.example.kora.data.dishes

import android.net.Uri
import android.util.Log
import com.example.kora.data.model.Dish
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID

class DishRepository {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val TAG = "DishRepository"

    fun addDish(
        restaurantId: String,
        name: String,
        category: String,
        price: String,
        description: String,
        prepTime: String,
        allergens: List<String>,
        customization: String,
        isAvailable: Boolean,
        imageUri: Uri?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d(TAG, "addDish called for Restaurant: $restaurantId, Name: $name")
        val dishId = UUID.randomUUID().toString()
        val userId = auth.currentUser?.uid ?: ""
        val timestamp = System.currentTimeMillis()

        fun saveToFirestore(imageUrl: String) {
            Log.d(TAG, "Preparing to save dish to Firestore. ImageURL: $imageUrl")

            val dish = Dish(
                id = dishId,
                restaurantId = restaurantId,
                name = name,
                category = category,
                price = price,
                description = description,
                prepTime = prepTime,
                allergens = allergens,
                customization = customization,
                isAvailable = isAvailable,
                imageUrl = imageUrl,
                createdBy = userId,
                createdAt = timestamp,
                updatedBy = userId,
                updatedAt = timestamp
            )
            db.collection("dishes").document(dishId).set(dish)
                .addOnSuccessListener {
                    db.collection("restaurants").document(restaurantId)
                        .update("dishCount", FieldValue.increment(1))
                        .addOnSuccessListener {
                            Log.d(TAG, "Success: Dish saved to firestore with ID: $dishId and restaurant count updated")
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Dish saved but failed to update count")
                            onSuccess()
                        }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Could not save to Firebase", e)
                    onError(e.message ?: "Failed to save dish")
                }
        }

        if (imageUri != null) {
            Log.d(TAG, "Image detected. Starting upload to Storage...")
            val ref = storage.reference.child("dish_images/$dishId.jpg")
            ref.putFile(imageUri)
                .addOnSuccessListener {
                    Log.d(TAG, "Image upload successful. Fetching download URL...")
                    ref.downloadUrl.addOnSuccessListener { uri -> saveToFirestore(uri.toString()) }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Image Upload failed", e)
                    onError("Image upload failed: ${e.message}")
                }
        } else {
            Log.d(TAG, "No image provided. Saving with empty URL")
            saveToFirestore("")
        }
    }

    fun getDishes(restaurantId: String): Flow<List<Dish>> = callbackFlow {
        Log.d(TAG, "Starting real-time listener for dishes in restaurant: $restaurantId")

        val registration = db.collection("dishes")
            .whereEqualTo("restaurantId", restaurantId)
            .addSnapshotListener { snapshot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                if (error != null) {
                    Log.e(TAG, "Listen failed.", error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    Log.d(TAG, "Snapshot received. Found ${snapshot.size()} dishes.")
                    val dishes = snapshot.toObjects(Dish::class.java)
                    trySend(dishes)
                } else {
                    Log.d(TAG, "snapshot was null")
                }
            }
        awaitClose {
            Log.d(TAG, "Closing dis listener")
            registration.remove()
        }
    }
}











