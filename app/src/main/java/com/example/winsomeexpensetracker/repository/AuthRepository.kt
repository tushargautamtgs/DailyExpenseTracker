package com.example.winsomeexpensetracker.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    fun login(email: String, password: String, onResult:(Result<FirebaseUser>)-> Unit){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val  user = auth.currentUser
                    if (user != null){
                        onResult(Result.success(user))
                    }else{
                        onResult(Result.failure(Exception("User is null")))
                    }
                }else{
                    onResult(Result.failure(task.exception ?: Exception("Login Failed")))
                }
            }
    }

    fun signUp(email: String, password: String, onResult: (Result<FirebaseUser>) -> Unit){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val user = auth.currentUser
                    if (user != null){

                        user.sendEmailVerification()
                        onResult(Result.success(user))
                    }else{
                        onResult(Result.failure(Exception("User is null")))
                    }
                }else{
                    onResult(Result.failure(task.exception ?: Exception("Registration Failed")))
                }
            }
    }

    fun logout(){
        auth.signOut()
    }

}