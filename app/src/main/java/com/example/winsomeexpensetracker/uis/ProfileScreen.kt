package com.example.winsomeexpensetracker.uis

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.winsomeexpensetracker.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val profile = profileViewModel.profile

    var name by remember(profile) { mutableStateOf(profile.name) }
    var occupation by remember(profile) { mutableStateOf(profile.occupation) }
    var mobileNumber by remember(profile) { mutableStateOf(profile.mobileNumber) }
    var saved by remember { mutableStateOf(false) }

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
                text = "My Profile",
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
                TextField(
                    value = name,
                    onValueChange = {
                        name = it
                        saved = false
                    },
                    label = { Text("Full Name", color = PremiumTextSecondary) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = PremiumTextPrimary,
                        unfocusedTextColor = PremiumTextPrimary,
                        focusedIndicatorColor = PremiumDivider,
                        unfocusedIndicatorColor = PremiumDivider,
                        cursorColor = PremiumCyanAccent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                TextField(
                    value = occupation,
                    onValueChange = {
                        occupation = it
                        saved = false
                    },
                    label = { Text("Occupation", color = PremiumTextSecondary) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = PremiumTextPrimary,
                        unfocusedTextColor = PremiumTextPrimary,
                        focusedIndicatorColor = PremiumDivider,
                        unfocusedIndicatorColor = PremiumDivider,
                        cursorColor = PremiumCyanAccent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                TextField(
                    value = mobileNumber,
                    onValueChange = { input ->
                        mobileNumber = input.filter { it.isDigit() }.take(10)
                        saved = false
                    },
                    label = { Text("Mobile Number", color = PremiumTextSecondary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = PremiumTextPrimary,
                        unfocusedTextColor = PremiumTextPrimary,
                        focusedIndicatorColor = PremiumDivider,
                        unfocusedIndicatorColor = PremiumDivider,
                        cursorColor = PremiumCyanAccent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        profileViewModel.updateProfile(
                            name = name.trim(),
                            occupation = occupation.trim(),
                            mobileNumber = mobileNumber.trim()
                        )
                        saved = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PremiumCyanAccent,
                        contentColor = PremiumBackground
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Save", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                if (saved) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Profile updated!",
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
