package com.example.currencies.search_filter

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


@Preview
@Composable
fun ActionFilter(onClick: () -> Unit = {},
                 label: @Composable () -> Unit = {Text("USD")},
                 selected: Boolean = false,
                 enabled: Boolean = true,
                 isLeadingIcon: Boolean = false) {
    return FilterChip(
        onClick = onClick,
        label =  label,
        enabled = enabled,
        selected = selected,
        leadingIcon = {
            if (selected && !isLeadingIcon) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "check mark",
                )
            }
        },
    )
}