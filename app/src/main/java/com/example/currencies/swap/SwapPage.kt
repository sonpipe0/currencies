package com.example.currencies.swap

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SyncAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun SwapPage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
    )
    {
        Text("Select Swap Input")
        SwapInputSelector(true)
        Icon(
            imageVector = Icons.Outlined.SyncAlt,
            contentDescription = "Swap Icon",
            modifier = Modifier.size(96.dp),
        )
        SwapInputSelector(false)
        Text("Select Swap Input")
    }
}