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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencies.R
import com.example.currencies.common.CustomFocusableTextField
import com.example.currencies.requests.currencyCodes
import com.example.currencies.search.components.SearchBar
import com.example.currencies.ui.theme.Border
import com.example.currencies.ui.theme.Box
import com.example.currencies.ui.theme.Icon
import com.example.currencies.ui.theme.Padding
import com.example.currencies.ui.theme.Radius
import com.example.currencies.ui.theme.zero
import kotlinx.coroutines.launch


@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun SwapInputSelector(
    enabled: Boolean = true,
    value: MutableState<String> = remember { mutableStateOf("") },
    code: MutableState<String> = remember { mutableStateOf("USD") },
    focusManager: FocusManager = LocalFocusManager.current,
    hideKeyBoard: MutableState<Boolean> = remember { mutableStateOf(false) }
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
        horizontalArrangement = Arrangement.spacedBy(zero),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(Box.Height.extraLarge)
            .border(
                width = Border.medium,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(Radius.small)
            )
            .clip(shape = RoundedCornerShape(Radius.small))
            .then(if (!enabled) Modifier.background(MaterialTheme.colorScheme.surfaceContainerHigh) else Modifier)
    ) {
        InputMenu(
            modifier = Modifier
                .padding(end = Padding.large)
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
                .padding(end = Padding.big)
                .weight(0.5f)
                .align(Alignment.CenterVertically),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
            ),
            textStyle = TextStyle(// ignore hardcoding because of reactiveFontSize
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
        horizontalArrangement = Arrangement.spacedBy(zero),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Image(
            painter = painterResource(id = currencyCodes[code.value]?.third ?: R.drawable.ic_launcher_background),
            contentDescription = code.value,
            modifier = Modifier.padding(Padding.large),
            colorFilter = if (code.value == "XAF" || code.value == "XCD") {
                androidx.compose.ui.graphics.ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
            } else {
                null
            }
        )
        Text(
            text = code.value,
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold,
            ),
        )
        Box(
            modifier = Modifier
                .padding(Padding.large),
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
                    modifier = Modifier.padding(vertical = Padding.large)
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
                        .padding(Padding.large)
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
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.SemiBold,
                                ),
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = Padding.small),
                                color = MaterialTheme.colorScheme.onSurface.copy(0.1f)
                            )
                            currentContinent = currency.value.first
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(zero),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Padding.small)
                                .clip(RoundedCornerShape(Radius.small))
                                .clickable {
                                    closeSheet(currency.key)
                                }
                        ) {
                            Image(
                                painter = painterResource(
                                    id = currency.value.third ?: R.drawable.ic_launcher_background
                                ),
                                contentDescription = currency.value.second,
                                modifier = Modifier.size(Icon.large),
                                colorFilter = if (currency.key == "XAF" || currency.key == "XCD") {
                                    androidx.compose.ui.graphics.ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                                } else {
                                    null
                                }
                            )
                            Column(
                                verticalArrangement = Arrangement.spacedBy(
                                    Padding.small,
                                    Alignment.CenterVertically
                                ),
                                modifier = Modifier
                                    .padding(start = Padding.large)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = currency.key,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontWeight = FontWeight.Normal,
                                    ),
                                )
                                Text(
                                    text = currency.value.second,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontWeight = FontWeight.Light,
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
