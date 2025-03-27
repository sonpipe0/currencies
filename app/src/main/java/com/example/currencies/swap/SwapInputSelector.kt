package com.example.currencies.swap

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencies.search.components.SearchBar
import com.example.currencies.utils.CurrencyMapper
import dropShadow


@Composable
fun SwapInputSelector(enabled: Boolean, hideKeyBoard: MutableState<Boolean>, focusManager: FocusManager) {
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
            hideKeyBoard = hideKeyBoard,
            focusManager = focusManager
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputMenu(modifier: Modifier, hideKeyBoard: MutableState<Boolean>, focusManager: FocusManager) {
    var expanded by remember { mutableStateOf(false) }
    var userInput by remember { mutableStateOf("USD") }
    val menuItemData = CurrencyMapper().getCurrencyData()
    var showBottomSheet by remember { mutableStateOf(false) }
    var searchBarText = remember {
        mutableStateOf("")
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Text(
            text = userInput,
            modifier = Modifier
                .padding(16.dp)
                .weight(1f),
            style = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
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
                    showBottomSheet = true
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
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true, confirmValueChange = { it != SheetValue.Hidden }),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize().verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                SearchBar(
                    text = searchBarText,
                    focusManager = focusManager,
                    hideKeyBoard = hideKeyBoard,

                )
                var currentContinent: String = ""
                menuItemData.forEach { currency ->
                    if (currency.value.second != currentContinent) {
                        currentContinent = currency.value.second
                        Text(
                            text = currentContinent ?: "",
                            modifier = Modifier.padding(16.dp),
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            ),
                        )
                    }

                    Row {
                        Image(
                            painter = painterResource(id = currency.value.fourth),
                            contentDescription = currency.value.first,
                            modifier = Modifier.size(24.dp),
                        )

                        Column {
                            Text(
                                text = currency.key,
                                modifier = Modifier.padding(16.dp),
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                ),
                            )
                            Text(
                                text = currency.value.first,
                                modifier = Modifier.padding(16.dp),
                                style = TextStyle(
                                    color = Color.Black.copy(0.5f),
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
