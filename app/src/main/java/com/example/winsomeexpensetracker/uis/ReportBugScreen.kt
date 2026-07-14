package com.example.winsomeexpensetracker.uis

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ReportBugScreen(navController: NavController) {
    var description by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }

    val db = remember { FirebaseFirestore.getInstance() }
    val auth = remember { FirebaseAuth.getInstance() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PremiumBackground)
            .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = PremiumTextPrimary)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Report Bug / Issue",
                color = PremiumTextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(PremiumCard)
                .border(1.dp, PremiumDivider, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "Found a bug or have feedback? Describe it below and we'll take a look.",
                    color = PremiumTextSecondary,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = description,
                    onValueChange = {
                        description = it
                        submitted = false
                    },
                    label = { Text("Describe the issue", color = PremiumTextSecondary) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = PremiumTextPrimary,
                        unfocusedTextColor = PremiumTextPrimary,
                        focusedIndicatorColor = PremiumDivider,
                        unfocusedIndicatorColor = PremiumDivider,
                        cursorColor = PremiumCyanAccent
                    ),
                    minLines = 5,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (description.isNotBlank()) {
                            isSubmitting = true
                            val report = hashMapOf(
                                "userId" to (auth.currentUser?.uid ?: "anonymous"),
                                "description" to description.trim(),
                                "timestamp" to System.currentTimeMillis()
                            )
                            db.collection("bug_reports")
                                .add(report)
                                .addOnSuccessListener {
                                    isSubmitting = false
                                    submitted = true
                                    description = ""
                                }
                                .addOnFailureListener { e ->
                                    isSubmitting = false
                                    Log.w("Firestore", "Error submitting bug report", e)
                                }
                        }
                    },
                    enabled = description.isNotBlank() && !isSubmitting,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PremiumCyanAccent,
                        contentColor = PremiumBackground
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (isSubmitting) "Submitting..." else "Submit Report",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                if (submitted) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Thanks! Your report has been submitted.",
                        color = PremiumCyanAccent,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}
