package com.example.currencies.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dropShadow


@Composable
fun SearchBar(text: MutableState<String> , hideKeyBoard: MutableState<Boolean> = remember { mutableStateOf(false) }) {
    Row(
       modifier = Modifier.fillMaxWidth()
           .dropShadow(
               shape = RoundedCornerShape(32.dp),
               color = Color.Black.copy(0.10f),
               blur = 4.dp,
           )
           .clip(RoundedCornerShape(32.dp))
           .background(color = Color.White)
           .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val focusManager = LocalFocusManager.current
        BasicTextField(
            value = text.value,
            onValueChange = {update ->
                if(update.length <23 ) text.value = update},
            modifier = Modifier.weight(1f).onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    hideKeyBoard.value = false
                }
                if (!focusState.isFocused) {
                    focusManager.clearFocus()
                }
            },
            singleLine = true,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 20.sp
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                focusManager.clearFocus()
                hideKeyBoard.value = true
                text.value = ""
            })
        )
        Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = "Search",
            tint = Color.Black,
            modifier = Modifier.clickable (
            ){
                focusManager.clearFocus()
                hideKeyBoard.value = true
                text.value = ""
            }
        )
        if(hideKeyBoard.value){
            focusManager.clearFocus()
        }
    }
}