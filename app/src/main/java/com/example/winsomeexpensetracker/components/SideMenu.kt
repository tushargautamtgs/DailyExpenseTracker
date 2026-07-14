package com.example.winsomeexpensetracker.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.winsomeexpensetracker.uis.PremiumCard
import com.example.winsomeexpensetracker.uis.PremiumCyanAccent
import com.example.winsomeexpensetracker.uis.PremiumDivider
import com.example.winsomeexpensetracker.uis.PremiumTextPrimary
import com.example.winsomeexpensetracker.uis.PremiumTextSecondary

@Composable
fun SideMenu(
    visible: Boolean,
    userName: String,
    onDismiss: () -> Unit,
    onProfileClick: () -> Unit,
    onHelpCenterClick: () -> Unit,
    onReportBugClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Dimmed background — tap anywhere here to close the menu
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(200)),
            exit = fadeOut(animationSpec = tween(200))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.55f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onDismiss() }
            )
        }

        // The sliding panel itself
        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(250)) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(200)) + fadeOut(),
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(280.dp)
                    .background(PremiumCard)
                    .padding(horizontal = 20.dp, vertical = 48.dp)
            ) {
                // --- Avatar + name header ---
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(PremiumCyanAccent.copy(alpha = 0.15f))
                        .border(1.dp, PremiumCyanAccent.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userName.trim().firstOrNull()?.uppercase() ?: "B",
                        color = PremiumCyanAccent,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = userName.ifBlank { "Buddy" },
                    color = PremiumTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))
                Divider(color = PremiumDivider)
                Spacer(modifier = Modifier.height(12.dp))

                SideMenuItem(icon = Icons.Default.Person, label = "Profile") {
                    onDismiss()
                    onProfileClick()
                }
                SideMenuItem(icon = Icons.Default.Info, label = "Help Center") {
                    onDismiss()
                    onHelpCenterClick()
                }
                SideMenuItem(icon = Icons.Default.Warning, label = "Report Bug / Issue") {
                    onDismiss()
                    onReportBugClick()
                }
            }
        }
    }
}

@Composable
private fun SideMenuItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = PremiumTextSecondary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, color = PremiumTextPrimary, fontSize = 15.sp)
    }
}
