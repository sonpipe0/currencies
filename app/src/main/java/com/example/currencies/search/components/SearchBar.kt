package com.example.currencies.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencies.common.CustomFocusableTextField
import dropShadow


@Composable
fun SearchBar(text: MutableState<String> , focusManager: FocusManager ,hideKeyBoard: MutableState<Boolean> = remember { mutableStateOf(false) }) {
    val style : TextStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp
            )
    Row(
       modifier = Modifier.fillMaxWidth()
           .height(56.dp)
           .padding(horizontal = 16.dp)
           .clip(RoundedCornerShape(32.dp))
           .background(color = MaterialTheme.colorScheme.surfaceContainerHigh)
           .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CustomFocusableTextField(
            value = text.value,
            onValueChange = { update ->
                if (update.length <= 23 || update.length < text.value.length) {
                    text.value = update
                }
            },
            modifier = Modifier.weight(1f),
            textStyle = style,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                focusManager.clearFocus()
                hideKeyBoard.value = true
                text.value = ""
            }),
            placeholder = "Search for a currency",
            placeholderStyle = style,
            focusManager = focusManager,
            hideKeyBoard = hideKeyBoard,
            enabled = true,
        )
        Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = "Search",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.clickable (
            ){
                focusManager.clearFocus()
                hideKeyBoard.value = true
                text.value = ""
            }
        )
    }
}