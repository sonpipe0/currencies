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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencies.utils.CurrencyMapper


@Composable
fun RatesComponent(
    baseCoin: String
) {
    val currencyMapper = CurrencyMapper();
    val currencies = currencyMapper.getContinents().flatMap { it ->
        currencyMapper.getCodesAndFlagsForContinent(it).take(2).filter {
            it.first != baseCoin
        }
    }
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .verticalScroll(rememberScrollState())
            .background(Color(0x40FFE0B2))
            .padding(horizontal = 16.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
    ) {
        currencies.map {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                ,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row (
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Image(
                        painter = painterResource(id = it.second),
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                    )
                    Text(
                        text = it.first,
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                Text(
                    text = "1.00 $baseCoin",
                    fontSize = 16.sp,
                    color = Color.Black,
                )
            }
        }

    }
}