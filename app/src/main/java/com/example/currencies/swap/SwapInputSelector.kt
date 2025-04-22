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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencies.R
import com.example.currencies.common.CustomFocusableTextField
import com.example.currencies.requests.currencyCodes
import com.example.currencies.search.components.SearchBar
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
    val reactiveFontSize = remember { mutableStateOf(30.sp) }
    LaunchedEffect(value.value) {
        if (value.value.length < 6) {
            reactiveFontSize.value = 30.sp
        }
        if (value.value.length >= 6) {
            reactiveFontSize.value = 18.sp
        }
        if (value.value.length >= 9) {
            reactiveFontSize.value = 14.sp
        }
    }
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
            .then(if (!enabled) Modifier.background(MaterialTheme.colorScheme.surfaceContainerHigh) else Modifier)
    ) {
        InputMenu(
            modifier = Modifier
                .padding(end = 16.dp)
                .fillMaxSize()
                .weight(1f)
                .align(Alignment.CenterVertically),
            code = code,
            hideKeyBoard = hideKeyBoard
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
                    reactiveFontSize.value = 30.sp
                }
                if (it.length >= 6) {
                    reactiveFontSize.value = 18.sp
                }
                if (it.length >= 9) {
                    reactiveFontSize.value = 14.sp
                }
                if(it.length < 11) {
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
                color = if(enabled) {
                    MaterialTheme.colorScheme.onBackground
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(0.5f)
                },
                fontWeight = FontWeight.SemiBold,
                fontSize = reactiveFontSize.value,
                textAlign = TextAlign.End
            ),
            focusManager = focusManager,
            hideKeyBoard = hideKeyBoard,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputMenu(
    modifier: Modifier,
    code: MutableState<String>,
    hideKeyBoard: MutableState<Boolean>
) {
    var expanded by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val searchInput = remember { mutableStateOf("") }
    val bottomSheet = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val scope = rememberCoroutineScope()
    val closeSheet: (selectedText: String) -> Unit = { selectedText ->
        scope.launch {
            bottomSheet.hide()
        }.invokeOnCompletion {
            scope.launch {
                code.value =
                    selectedText // there's a bug if you set it before the hide with the bottomSheet animation
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
            painter = painterResource(id = currencyCodes[code.value]?.third ?: R.drawable.ic_launcher_background),
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
            Column {
                val focusManager: FocusManager = LocalFocusManager.current
                Column(
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    SearchBar(
                        text = searchInput,
                        focusManager = focusManager,
                        isOutlined = true,
                        isRounded = false,
                        hideKeyBoard = hideKeyBoard
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    var currentContinent = ""
                    currencyCodes.filter { it.value.third != 0 }.filter {
                        it.key.lowercase()
                            .startsWith(searchInput.value) || it.value.second.lowercase()
                            .startsWith(
                                searchInput.value.lowercase()
                            )
                    }.forEach { currency ->
                        if (currentContinent != currency.value.first) {
                            Text(
                                text = currency.value.first,
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 20.sp,
                                ),
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 8.dp),
                                color = MaterialTheme.colorScheme.onSurface.copy(0.1f)
                            )
                            currentContinent = currency.value.first
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(0.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    closeSheet(currency.key)
                                }
                        ) {
                            Image(
                                painter = painterResource(
                                    id = currency.value.third ?: R.drawable.ic_launcher_background
                                ),
                                contentDescription = currency.value.second,
                                modifier = Modifier.size(24.dp),
                                colorFilter = if (currency.key == "XAF" || currency.key == "XCD") {
                                    androidx.compose.ui.graphics.ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                                } else {
                                    null
                                }
                            )
                            Column(
                                verticalArrangement = Arrangement.spacedBy(
                                    8.dp,
                                    Alignment.CenterVertically
                                ),
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = currency.key,
                                    style = TextStyle(
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 20.sp,
                                    ),
                                )
                                Text(
                                    text = currency.value.second,
                                    style = TextStyle(
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontWeight = FontWeight.Thin,
                                        fontSize = 12.sp,
                                    ),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
