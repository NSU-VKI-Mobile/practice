import android.widget.NumberPicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

val colorCollection = mapOf(
    Color.Red to Color(0xFFFFEBEE),
    Color.Green to Color(0x8098FB98),
    Color.Blue to Color(0x806495ED)
)



@Composable
fun OpSlider(value: Int,
             onValueChange: (Float) -> Unit,
             color : Color) {
    Slider(
        value = value.toFloat(),
        valueRange = 0f..255f,
        onValueChange = onValueChange,
        colors = SliderDefaults.colors(
            thumbColor = color,
            inactiveTickColor = colorCollection.getOrDefault(color, Color.Gray),
            activeTrackColor = color
        )
    )
}

@Composable
fun MyScreen(
    viewModel: ColorPickerViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        OpSlider(uiState.red, {viewModel.onRedChanged(it)}, Color.Red)
        Text(text = "${uiState.red}")
        OpSlider(uiState.green, {viewModel.onGreenChanged(it)}, Color.Green)
        Text(text = "${uiState.green}")
        OpSlider(uiState.blue, {viewModel.onBlueChanged(it)}, Color.Blue)
        Text(text = "${uiState.blue}")
        Box(
            modifier = Modifier.fillMaxWidth().height(50.dp).background(uiState.pickedColor)
        )
        Text(text = "номер цвета: ${uiState.hexCode}")

        Button(onClick = {
            viewModel.generateRandomColor()
        },
            modifier = Modifier.fillMaxWidth().padding(top = 100.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color.Gray
            )
        ) {
            Text("рандомный цвет")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowMyScreen() {
    MyScreen()
}