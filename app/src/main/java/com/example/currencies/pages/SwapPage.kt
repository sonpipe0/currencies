package com.example.currencies.pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SyncAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencies.search.components.roundToDigits
import com.example.currencies.swap.SwapPageInstructions
import com.example.currencies.swap.SwapInputSelector
import com.example.currencies.viewmodels.ExchangeRateViewModel
import dropShadow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SwapPage(hideKeyBoard: MutableState<Boolean>) {
    val numberInput: MutableState<String> = remember { mutableStateOf("0") }
    val codeInput: MutableState<String> = remember { mutableStateOf("USD") }
    val numberOutput: MutableState<String> = remember { mutableStateOf("0") }
    val codeOutput: MutableState<String> = remember { mutableStateOf("EUR") }
    var rotationAngle by remember { mutableFloatStateOf(90f) }
    val exchangeRateViewModel = hiltViewModel<ExchangeRateViewModel>()
    val exchangeRate by exchangeRateViewModel.currentExchangeRate.collectAsState()
    val animatedRotationAngle by animateFloatAsState(
        targetValue = rotationAngle,
        animationSpec = tween(
            durationMillis = 1000,
        )
    )
    val scope = rememberCoroutineScope()

    LaunchedEffect(codeInput.value, codeOutput.value, numberInput.value) {
        if (codeInput.value != codeOutput.value) {
            exchangeRateViewModel.fetchExchangeRates(codeInput.value, codeOutput.value)
            println("Fetching exchange rate for ${codeInput.value} to ${codeOutput.value}")
            println("Exchange rate: $exchangeRate")
            numberOutput.value = numberInput.value.toDoubleOrNull()?.let {
                if (it > 0) {
                    (it * exchangeRate).roundToDigits(1).toString()
                } else {
                    "0"
                }
            } ?: "0"
        }
    }

    val context  = LocalContext.current
    Column(
        modifier = Modifier
            .padding(top = 64.dp, bottom = 16.dp, start = 32.dp, end = 32.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
    ) {
        val focusManager: FocusManager = LocalFocusManager.current
        Text(
            text = "Swap",
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier
                .wrapContentSize()
                .padding(vertical = 32.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
            ) {
                SwapInputSelector(true, numberInput, codeInput, focusManager, hideKeyBoard)
                SwapInputSelector(false, numberOutput, codeOutput,focusManager, hideKeyBoard)
            }
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .offset(x = (-10).dp)
                    .dropShadow(
                        color = Color.Black.copy(0.1f),
                        offsetX = 0.dp,
                        offsetY = 0.dp,
                        blur = 8.dp,
                        shape = CircleShape,
                    )
                    .border(
                        width = 1.dp,
                        color = Color.Black.copy(0.1f),
                        shape = CircleShape,
                    )
                    .rotate(animatedRotationAngle)
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.tertiaryContainer)
                    .padding(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.SyncAlt,
                    contentDescription = "Swap Icon",
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            scope.launch {
                                rotateAnimation(rotationAngle, 800) { newAngle ->
                                    rotationAngle = newAngle
                                }
                            }.invokeOnCompletion {
                                val temp = codeInput.value
                                codeInput.value = codeOutput.value
                                codeOutput.value = temp
                                val tempNumber = numberInput.value
                                numberInput.value = numberOutput.value
                                numberOutput.value = tempNumber
                            }
                        }
                )
            }
        }
        // print the exchange rate
        Text(
            text = "Exchange rate: ${exchangeRate.roundToDigits(2)}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        SwapPageInstructions(baseCoin = "USD")
    }
}

suspend fun rotateAnimation(currentAngle: Float, duration: Long, onAnimationEnd: (Float) -> Unit) {
    val newAngle = currentAngle + 180f
    onAnimationEnd(newAngle)
    delay(duration)
}
