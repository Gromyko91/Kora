package com.example.kora.data.auth

import android.util.Log
import com.example.kora.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val TAG = "AuthRepository"

    // -----  SIGN UP -----
    fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d(TAG, "Attempting Sign Up for: $email")
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid
                if (uid == null) {
                    Log.e(TAG, "Sign Up Error: UID is null")
                    onError("User creation failed")
                    return@addOnSuccessListener
                }

                Log.d(TAG, "Auth created successfully, Savind data to Firestore...")

                val currentTimestamp = System.currentTimeMillis()

                val user = User(
                    uid = uid,
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    phone = phone,
                    role = "user",
                    createdAt = currentTimestamp,
                    updatedAt = currentTimestamp
                )

                db.collection("users").document(uid).set(user)
                    .addOnSuccessListener {
                        Log.d(TAG, "Firestore USer Data Saved Successfully")
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Firestore Save Failed: ${e.message}")
                        onError(e.message ?: "Failed to save user Data")
                    }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "ForebaseAuth Failed: ${e.message}")
                onError(e.message ?: "Sign up Failed")
            }
    }

    fun signIn(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d(TAG, "Attempting Sign in for: $email")

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid
                Log.d(TAG, "Auth credentials valid. Fetching user role for UID: $uid")

                if (uid != null) {
                    getUserRole(uid,
                        onSuccess = { role ->
                            Log.d(TAG, "Role fetched: $role. Login Complete.")
                            onSuccess(role)
                        },
                        onError = { error ->
                            Log.e(TAG, "Role fetch Failed: $error")
                            onError(error)
                        }
                        )
                } else {
                    onError("User ID not found")
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "SignIN Failed: ${e.message}")
                onError(e.message ?: "Login Failed")
            }
    }

    fun getUserRole(
        uid: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    val role = doc.getString("role") ?: "user"
                    onSuccess(role)
                } else {
                    Log.w(TAG, "User document does not exist for uid: $uid")
                    onSuccess("user")
                }
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Failed to fetch role")
            }
    }

    fun sendPasswordReset(
        email: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d(TAG, "Sending password reset email to: $email")
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                Log.d(TAG, "Reset email Sent Successfully")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to send reset email: ${e.message}")
                onError(e.message ?: "Failed to send reset email")
            }
    }

    fun getCurrentUserData(
        onSuccess: (User) -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    val user = document.toObject(User::class.java)
                    if (user != null) {
                        onSuccess(user)
                    } else {
                        onError("User data not found")
                    }
                }
                .addOnFailureListener {
                    onError(it.message ?: "Failed to fetch user data")
                }
        } else {
            onError("No user logged in")
        }
    }

    fun logout() {
        Log.d(TAG, "User Signing out")
        auth.signOut()
    }

//    fun currentUserUid(): String? = auth.currentUser?.uid
}