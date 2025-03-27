package com.example.currencies.swap

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SyncAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp


@Composable
fun SwapPage(hideKeyBoard: MutableState<Boolean>) {
    val focusManager: FocusManager = LocalFocusManager.current
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
    )
    {
        Text("Select Swap Input")
        SwapInputSelector(true, hideKeyBoard, focusManager)
        Icon(
            imageVector = Icons.Outlined.SyncAlt,
            contentDescription = "Swap Icon",
            modifier = Modifier.size(96.dp),
        )
        SwapInputSelector(false, hideKeyBoard, focusManager)
        Text("Select Swap Input")
    }
}