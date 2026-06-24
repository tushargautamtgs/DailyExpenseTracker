package com.example.winsomeexpensetracker.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.winsomeexpensetracker.model.Category

@Composable
fun RadialFilterMenu(
    selectedCategory: Category?,
    onCategorySelected: (Category?) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .size(220.dp)
    ) {

        FloatingActionButton(
            onClick = {
                onCategorySelected(Category.FOOD)
            },
            modifier = Modifier
                .offset(x = 120.dp, y = 0.dp)
                .size(52.dp)
        ) {
            Text("🍔")
        }

        FloatingActionButton(
            onClick = {
                onCategorySelected(Category.TRAVEL)
            },
            modifier = Modifier
                .offset(x = 80.dp, y = 45.dp)
                .size(52.dp)
        ) {
            Text("✈️")
        }

        FloatingActionButton(
            onClick = {
                onCategorySelected(Category.SHOPPING)
            },
            modifier = Modifier
                .offset(x = 50.dp, y = 100.dp)
                .size(52.dp)
        ) {
            Text("🛍️")
        }

        FloatingActionButton(
            onClick = {
                onCategorySelected(Category.BILLS)
            },
            modifier = Modifier
                .offset(x = 50.dp, y = 180.dp)
                .size(52.dp)
        ) {
            Text("💡")
        }

        FloatingActionButton(
            onClick = {
                onCategorySelected(Category.ENTERTAINMENT)
            },
            modifier = Modifier
                .offset(x = 80.dp, y = 235.dp)
                .size(52.dp)
        ) {
            Text("🎬")
        }

        FloatingActionButton(
            onClick = {
                onCategorySelected(null)
            },
            modifier = Modifier
                .offset(x = 0.dp, y = 120.dp)
                .size(72.dp)
        ) {
            Text(
                selectedCategory?.name?.take(3)
                    ?: "ALL"
            )
        }
    }
}