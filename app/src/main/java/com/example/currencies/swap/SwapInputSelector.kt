package com.example.currencies.swap

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencies.R
import com.example.currencies.common.CustomFocusableTextField
import kotlinx.coroutines.launch


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SwapInputSelector(
    enabled: Boolean,
    value: MutableState<String>,
    code: MutableState<String>,
    focusManager: FocusManager,
    hideKeyBoard: MutableState<Boolean>
) {
    return Row(
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(shape = RoundedCornerShape(8.dp))
    ) {
        InputMenu(
            modifier = Modifier
                .padding(end = 16.dp)
                .fillMaxSize()
                .weight(1f)
                .align(Alignment.CenterVertically),
            code = code,
        )
        CustomFocusableTextField(
            value = if (!enabled && value.value == "") {
                "0"
            } else {
                value.value
            },
            onValueChange = {
                if (it.any { char -> !char.isDigit() && !char.isWhitespace() }) {
                    return@CustomFocusableTextField
                }
                if (it.length < 6) {
                    value.value = it
                }
            },
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 12.dp)
                .weight(0.5f)
                .align(Alignment.CenterVertically),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
            ),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold,
                fontSize = 30.sp,
                textAlign = TextAlign.End
            ),
            focusManager = focusManager,
            hideKeyBoard = hideKeyBoard,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputMenu(modifier: Modifier, code: MutableState<String>) {
    var expanded by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheet = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val scope = rememberCoroutineScope()
    val closeSheet: (selectedText: String) -> Unit = { selectedText ->
        scope.launch {
            bottomSheet.hide()
        }.invokeOnCompletion {
            scope.launch {
                code.value = selectedText // there's a bug if you set it before the hide with the bottomSheet animation
                showBottomSheet = false
            }
        }
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = code.value,
            modifier = Modifier.padding(16.dp),
            colorFilter = if (code.value == "XAF" || code.value == "XCD") {
                androidx.compose.ui.graphics.ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
            } else {
                null
            }
        )
        Text(
            text = code.value,
            style = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
            ),
        )
        Box(
            modifier = Modifier
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            IconButton(
                onClick = {
                    showBottomSheet = true;
                },
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "currencies",
                )
            }
        }
    }


    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            modifier = Modifier.fillMaxSize(),
            sheetState = bottomSheet,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                }
        }
    }
}
