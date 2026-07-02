package com.example.winsomeexpensetracker.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.winsomeexpensetracker.model.Expense
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

private val CardColor = Color(0xFF202B38)
private val BorderColor = Color(0xFF384554)
private val Accent = Color(0xFF62D0C5)
private val TextPrimary = Color.White
private val TextSecondary = Color(0xFF8E9BA8)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SpendingTimelineCard(
    expenses: List<Expense>
) {

    var currentMonth by remember {
        mutableStateOf(YearMonth.now())
    }

    var selectedDay by remember {
        mutableStateOf(LocalDate.now().dayOfMonth)
    }
    val firstDayOfMonth = currentMonth.atDay(1)
    val firstDayOffset = firstDayOfMonth.dayOfWeek.value - 1 // Monday = 0

    val totalCells = firstDayOffset + currentMonth.lengthOfMonth()

    val calendarCells = List(totalCells) { index ->
        if (index < firstDayOffset) null

        else index - firstDayOffset + 1
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardColor
        ),
        shape = RoundedCornerShape(20.dp)
    ) {

        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp)
        ) {

            Text(
                text = "Spending Timeline",
                color = TextPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = {
                        currentMonth = currentMonth.minusMonths(1)
                    }
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowLeft,
                        null,
                        tint = Accent
                    )
                }

                Text(
                    text = currentMonth.month.getDisplayName(
                        TextStyle.FULL,
                        Locale.getDefault()
                    ) + " ${currentMonth.year}",
                    modifier = Modifier.weight(1f),
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = {
                        currentMonth = currentMonth.plusMonths(1)
                    }
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowRight,
                        null,
                        tint = Accent
                    )
                }

            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                listOf(
                    "Mo",
                    "Tu",
                    "We",
                    "Th",
                    "Fr",
                    "Sa",
                    "Su"
                ).forEach {

                    Text(
                        text = it,
                        color = TextSecondary,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.width(38.dp),
                    )
                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                userScrollEnabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(215.dp)
            ) {

                items(calendarCells) { day ->

                    if (day == null) {

                        Spacer(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(38.dp)
                        )

                    } else {

                        val selected = day == selectedDay

                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(38.dp)
                                .background(
                                    if (selected) Accent else Color.Transparent,
                                    CircleShape
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (selected) Accent else BorderColor,
                                    shape = CircleShape
                                )
                                .clickable {
                                    selectedDay = day
                                },
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = day.toString(),
                                color = if (selected)
                                    Color.Black
                                else
                                    TextPrimary,
                                style = MaterialTheme.typography.bodyMedium
                            )

                        }

                    }

                }

            }

        }

    }

}