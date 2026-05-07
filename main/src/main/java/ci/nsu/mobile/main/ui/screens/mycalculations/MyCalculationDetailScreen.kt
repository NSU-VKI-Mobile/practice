package ci.nsu.mobile.main.ui.screens.mycalculations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ci.nsu.mobile.main.data.database.DepositCalculation
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MyCalculationDetailScreen(
    calculation: DepositCalculation?,
    isDeleting: Boolean,
    onDelete: () -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("← Назад")
        }

        if (calculation == null) {
            Text("Расчёт не найден", modifier = Modifier.padding(16.dp))
            return
        }

        // остальное содержание карточки (без кнопки удаления, она будет внизу)

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onDelete,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            if (isDeleting) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text("Удалить расчёт")
            }
        }
    }
}