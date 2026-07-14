package com.example.winsomeexpensetracker.uis

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

private data class Faq(val question: String, val answer: String)

private val faqs = listOf(
    Faq(
        "How do I add an expense?",
        "Use the Add Expense card on the home screen — enter a title, amount, and category, then tap Add Expense."
    ),
    Faq(
        "How do I change my monthly budget?",
        "Tap the pencil icon on the Monthly Summary card and enter a new amount."
    ),
    Faq(
        "How does the spending calendar work?",
        "Each day is colored based on how much you spent compared to your daily target. Tap any day to see or add an expense for that date."
    ),
    Faq(
        "Is my data backed up?",
        "Yes — all expenses are stored in the cloud and linked to your account, so they're available whenever you log in."
    )
)

@Composable
fun HelpCenterScreen(navController: NavController) {
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
                text = "Help Center",
                color = PremiumTextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            faqs.forEach { faq ->
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
                            text = faq.question,
                            color = PremiumTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = faq.answer,
                            color = PremiumTextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
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
                        text = "Still need help?",
                        color = PremiumTextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Reach out to us at support@winsomeexpensetracker.com",
                        color = PremiumTextSecondary,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
