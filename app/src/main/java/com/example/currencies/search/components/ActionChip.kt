package com.example.currencies.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ActionChip(
    value: @Composable () -> Unit,
    onClick: () -> Unit,
    selected: Boolean = false,
) {
    Box(
        modifier = Modifier
            .height(32.dp)
            .clip(RoundedCornerShape(CornerSize(50)))
            .clickable(!selected) { onClick() }
            .background(if (selected) MaterialTheme.colorScheme.onSurface.copy(0.12f) else Color.Transparent)
            .border(
                width = 1.dp,
                color = if (!selected) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                shape = RoundedCornerShape(CornerSize(50))
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        value()
    }

}