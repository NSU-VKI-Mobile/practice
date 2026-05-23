package ci.nsu.mobile.main.ui.components

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.data.room.DepositCalculationEntity
import java.util.Date

@Composable
fun ShortHistoryItemCard(deposit: DepositCalculationEntity, dateFormat: SimpleDateFormat, Click: ()-> Unit) {
    Card(modifier = Modifier.padding(20.dp).clickable(onClick = Click).width(400.dp)) {
        Text("DEPOSIT #${deposit.id}",
            modifier = Modifier.padding(10.dp))
        Text("Итоговая сумма вклада:${String.format("%.2f", deposit.finalAmount)}₽",
            modifier = Modifier.padding(horizontal = 10.dp))
        Text("Процентная ставка:${deposit.interestRate}%",
            modifier = Modifier.padding(horizontal = 10.dp))
        Text(dateFormat.format(Date(deposit.calculationDate)),
            modifier = Modifier.padding(10.dp))
    }
}