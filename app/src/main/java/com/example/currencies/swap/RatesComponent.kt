package com.example.currencies.swap

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencies.R
import com.example.currencies.ui.theme.Padding
import com.example.currencies.ui.theme.Radius


@Composable
fun SwapPageInstructions(
    baseCoin: String
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(Radius.small))
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(horizontal = Padding.large)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Padding.small, Alignment.Top),
    ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Padding.small)
                ,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "This is the swap page\nWhere you can exchange your currency for other currencies.\n" +
                            "1. Select your target currency\n" +
                            "2. Enter the amount to swap\n" +
                            "3. Review the exchange rate\n" +
                            "4. Confirm your transaction",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                    ),
                    modifier = Modifier.padding(vertical = Padding.small)
                )
            }

    }
}