package com.example.currencies.swap

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dropShadow


@Composable
fun SwapInputSelector(enabled: Boolean) {
    return Row(
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .dropShadow(
                shape = RoundedCornerShape(16.dp),
                color = Color.Black.copy(0.25f),
                blur = 4.dp,
            )
            .height(49.dp)
            .clip(shape = RoundedCornerShape(16.dp))
            .background(color = Color.White)
    ) {
        BasicTextField(
            value = "1000000",
            onValueChange = { },
            enabled = enabled,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
                .weight(1f),
            singleLine = true,
            textStyle =
            TextStyle(
                color = Color.Black,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            ),
        )
        VerticalDivider(
            color = Color.Black.copy(0.75f),
            modifier = Modifier.width(1.dp),
        )
        InputMenu(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f)
                .fillMaxSize()
                .align(Alignment.CenterVertically),
        )
    }
}


@Composable
fun InputMenu(modifier: Modifier) {
    var expanded by remember { mutableStateOf(false) }
    var userInput by remember { mutableStateOf("USD") }
    val menuItemData = arrayOf("USD", "EUR", "JPY", "GBP", "AUD", "CAD", "CHF", "CNY", "SEK", "NZD")
    return Row(
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        BasicTextField(
            value = userInput,
            onValueChange = { userInput = it },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
                .weight(1f),
            singleLine = true,
            textStyle =
            TextStyle(
                color = Color.Black,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            ),
        )
        Box(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            IconButton(
                onClick = {
                    expanded = !expanded
                    if (userInput.length >= 3) {
                        userInput = ""
                    }
                },
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "currencies",
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                menuItemData.filter {
                    it.startsWith(userInput.uppercase())
                }.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = { userInput = option; expanded = false },
                    )
                }
            }
        }

    }
}