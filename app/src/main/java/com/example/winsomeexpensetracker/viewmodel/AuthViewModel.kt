package com.example.winsomeexpensetracker.viewmodel

import android.os.Message
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.winsomeexpensetracker.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


sealed class AuthState{
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: FirebaseUser): AuthState()
    data class Error(val message: String): AuthState()
}

class AuthViewModel : ViewModel() {

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
    private val repository = AuthRepository()

    private val _authState = mutableStateOf<AuthState>(AuthState.Idle)

    val authState: State<AuthState> = _authState

    val isUserLoggedIn: Boolean
        get() = repository.currentUser != null

    fun login(email: String, password: String){
        if(email.isBlank() || password.isBlank()){
            _authState.value = AuthState.Error("Email and Password cannot be empty")
            return
        }
        _authState.value = AuthState.Loading
        repository.login(email,password) {
            result ->
            result.fold(
                onSuccess = {user -> _authState.value = AuthState.Success(user)},
                onFailure = {exception -> _authState.value = AuthState.Error(exception.localizedMessage ?: "Login Failed")}
            )
        }
    }

    fun signUp(email: String, password: String){
        if (email.isBlank() || password.isBlank()){
            _authState.value = AuthState.Error("Email and Password cannot be empty")
            return
        }

        _authState.value = AuthState.Loading
        repository.signUp(email, password){ result ->
            result.fold(
                onSuccess = {user -> _authState.value = AuthState.Success(user)},
                onFailure = {exception -> _authState.value = AuthState.Error(exception.localizedMessage ?: "SignUp Failed Please Try again")}
            )
        }
    }

    fun clearState(){
        _authState.value = AuthState.Idle
    }




}