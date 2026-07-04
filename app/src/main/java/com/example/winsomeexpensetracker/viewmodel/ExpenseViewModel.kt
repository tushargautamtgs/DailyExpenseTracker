package com.example.winsomeexpensetracker.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.winsomeexpensetracker.model.Category
import com.example.winsomeexpensetracker.model.Expense
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query // <--- THIS IS THE MISSING IMPORT!

class ExpenseViewModel : ViewModel() {

    // The UI will still read from this list, but now it's powered by Firebase!
    val expenses = mutableStateListOf<Expense>()

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    init {
        // As soon as the ViewModel starts, pull the user's data from the cloud
        fetchExpenses()
    }

    private fun fetchExpenses() {
        val userId = auth.currentUser?.uid ?: return

        // Listen for real-time updates from this specific user's Firestore collection
        db.collection("users").document(userId).collection("expenses")
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firestore", "Listen failed.", error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    expenses.clear()
                    for (doc in snapshot.documents) {
                        // Map the Firestore document back into your Expense data class
                        val expense = Expense(
                            documentId = doc.id, // Generate a local ID
                            title = doc.getString("title") ?: "",
                            amount = doc.getDouble("amount") ?: 0.0,
                            category = Category.valueOf(doc.getString("category") ?: "FOOD"),
                            date = doc.getLong("date") ?: 0L
                        )
                        expenses.add(expense)
                    }
                }
            }
    }

    // `date` defaults to "now" so every existing call site (Add Expense button, etc.)
    // keeps working unchanged. Pass an explicit epoch-millis value to log an expense
    // against a past (or future) date, e.g. from the calendar day popup.
    fun addNewExpense(
        title: String,
        amount: Double,
        category: Category,
        date: Long = System.currentTimeMillis()
    ) {
        val userId = auth.currentUser?.uid ?: return

        // Create a hashmap representing the data we want to save
        val expenseData = hashMapOf(
            "title" to title,
            "amount" to amount,
            "category" to category.name,
            "date" to date
        )
        // Push it to Cloud Firestore under the logged-in user's UID
        db.collection("users").document(userId).collection("expenses")
            .add(expenseData)
            .addOnSuccessListener {
                Log.d("Firestore", "Expense successfully added!")

                fetchExpenses()
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding expense", e)
            }
    }

    // Add this function to your ViewModel:
    fun removeExpense(expense: Expense) {
        val userId = auth.currentUser?.uid ?: return

        // Use the documentId we saved to delete the exact document
        db.collection("users").document(userId).collection("expenses")
            .document(expense.documentId)
            .delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Document successfully deleted!")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error deleting document", e)
            }
    }

    fun totalByCategory(category: Category): Double {
        return expenses
            .filter { it.category == category }
            .sumOf { it.amount }
    }

    fun clearData(){
        expenses.clear()
    }

}
