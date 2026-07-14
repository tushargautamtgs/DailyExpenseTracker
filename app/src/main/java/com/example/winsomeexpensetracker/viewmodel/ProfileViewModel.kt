package com.example.winsomeexpensetracker.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.winsomeexpensetracker.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class ProfileViewModel : ViewModel() {

    var profile by mutableStateOf(UserProfile())
        private set

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    init {
        fetchProfile()
    }

    private fun fetchProfile() {
        val userId = auth.currentUser?.uid ?: return

        // Same "users/{uid}" document that ExpenseViewModel's expenses subcollection lives under.
        db.collection("users").document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Profile listen failed.", error)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    profile = UserProfile(
                        name = snapshot.getString("name") ?: "",
                        occupation = snapshot.getString("occupation") ?: "",
                        mobileNumber = snapshot.getString("mobileNumber") ?: ""
                    )
                }
            }
    }

    fun updateProfile(name: String, occupation: String, mobileNumber: String) {
        val userId = auth.currentUser?.uid ?: return

        val profileData = hashMapOf(
            "name" to name,
            "occupation" to occupation,
            "mobileNumber" to mobileNumber
        )

        // merge() so this doesn't wipe out any other fields already on the user doc.
        db.collection("users").document(userId)
            .set(profileData, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("Firestore", "Profile updated successfully!")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error updating profile", e)
            }
    }

    fun clearData() {
        profile = UserProfile()
    }
}
