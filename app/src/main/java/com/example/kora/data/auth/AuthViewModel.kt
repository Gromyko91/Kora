package com.example.kora.data.auth

import androidx.lifecycle.ViewModel
import com.example.kora.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun signUp(firstName: String, lastName: String, email: String, phone: String, password: String) {
        _authState.value = AuthState.Loading
        repository.signUp(firstName, lastName, email, phone, password,
            onSuccess = {
                _authState.value = AuthState.Success("user")
            },
            onError = { error ->
                _authState.value = AuthState.Error(error)
            }
        )
    }

    fun signIn(email: String,  password: String) {
        _authState.value = AuthState.Loading
        repository.signIn(email, password,
            onSuccess = { role ->
                _authState.value = AuthState.Success(role)
            },
            onError = { error ->
                _authState.value = AuthState.Error(error)
            }
        )
    }

    fun resetPassword(email: String) {
        _authState.value = AuthState.Loading
        repository.sendPasswordReset(email,
            onSuccess = {
                _authState.value = AuthState.PasswordResetEmailSent
            },
            onError = { error ->
                _authState.value = AuthState.Error(error)
            }
        )
    }

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    fun fetchCurrentUser() {
        repository.getCurrentUserData(
            onSuccess = { user ->
                _currentUser.value = user
            },
            onError = { error ->
                _authState.value = AuthState.Error(error)
            }
        )
    }

    fun logout() {
        repository.logout()
        _authState.value = AuthState.Idle
        _currentUser.value = null
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val role: String) : AuthState()
    object PasswordResetEmailSent : AuthState()
    data class Error(val message: String) : AuthState()
}