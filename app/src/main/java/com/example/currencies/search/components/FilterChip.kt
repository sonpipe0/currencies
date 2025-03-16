package com.example.currencies.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FilterChip(filter: String, color: Color,textColor: Color, isRounded: Boolean = false ,onClick: () -> Unit) {

    Box(
        modifier = Modifier
            .clip(if (isRounded) RoundedCornerShape(percent = 100) else RoundedCornerShape(32.dp))
            .background(color)
            .clickable(onClick = onClick)
            .then(
                if (isRounded) Modifier.width(32.dp).height(32.dp) else Modifier
            ).then(
                if (isRounded) Modifier.padding(0.dp) else Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = filter,
            color = textColor,
            textAlign = TextAlign.Center,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}