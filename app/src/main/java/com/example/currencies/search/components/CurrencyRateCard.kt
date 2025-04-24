package com.example.currencies.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastRoundToInt
import com.example.currencies.search.types.CurrencyRate
import com.example.currencies.ui.theme.Border
import com.example.currencies.ui.theme.Box
import com.example.currencies.ui.theme.Padding
import java.util.Locale
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.round

fun Double.roundToDigits(digits: Int): Double {
    val factor = 10.0.pow(digits.toDouble())
    return kotlin.math.round(this * factor) / factor
}
@Composable
fun CurrencyRateCard(currencyRate: CurrencyRate, baseCurrencyCode: String = "USD",onFavorite: () -> Unit, isFavorite: Boolean = false ,last: Boolean = false) {
    val positive: Boolean = currencyRate.dailyChangePercentage <= 0
    val onPrimaryContainerColor: Color = MaterialTheme.colorScheme.onSurface
    return Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .then(
                if (!last) Modifier.drawBehind {
                    drawLine(
                        color = onPrimaryContainerColor,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height)
                    )
                } else Modifier
            )
            .height(Box.Height.large)
            .padding(
                Padding.medium
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                modifier = Modifier
                    .clickable { onFavorite() },
                imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.StarOutline,
                contentDescription = "Star",
                tint = MaterialTheme.colorScheme.onSurface
            )
            Column(
                modifier = Modifier.width(Box.Width.large),
                verticalArrangement = Arrangement.spacedBy(Padding.extraSmall),
            ) {
                Text(
                    currencyRate.code,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                )
                Text(
                    currencyRate.name,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "1 $baseCurrencyCode  = ${currencyRate.value} ${currencyRate.code}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface,
                ))
            var text = "${(currencyRate.dailyChangePercentage * -1).roundToDigits(2).toString()} %"

            Text(if (text == "-0.0 %") "0.00 %" else text,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = if (positive) Color(0xff2FD905) else MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}
