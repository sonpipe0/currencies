package com.example.currencies.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.currencies.search.types.CurrencyRate
import java.util.Locale

@Composable
fun CurrencyRateCard(currencyRate: CurrencyRate, baseCurrencyCode: String = "USD", last: Boolean = false) {
    val positive: Boolean = currencyRate.dailyChangePercentage > 0
    return Row (
        modifier = Modifier
            .fillMaxWidth().background(Color.White)
            .then(
                if (!last) Modifier.drawBehind {
                    val borderSize = 0.5.dp
                    drawLine(
                        color = Color.Black,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height)
                    )
                } else Modifier
            ).
            height(68.dp).padding(
                10.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column (
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ){
            Text(currencyRate.code,
                style = TextStyle(
                    fontSize = 16.sp,
                )
            )
            Text(currencyRate.name,
                style = TextStyle(fontSize = 10.sp)
            )
        }
        Column (
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween
        ){
            Text("${currencyRate.value} $baseCurrencyCode",
                style = TextStyle(
                    fontSize = 16.sp,
                ))
            Text("${if(positive )"+" else "-"} ${String.format(Locale.getDefault(), "%.2f", currencyRate.dailyChangePercentage * 100).substring(1)} %",
                style = TextStyle(
                    color = if (positive) Color(0xff2FD905) else Color.Red,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                ))
        }
    }
}
