package com.example.flightcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FlightCalculatorScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightCalculatorScreen() {
    var emptyWeight by remember { mutableStateOf("8700") }
    var maxWeight by remember { mutableStateOf("13000") }
    var crewCount by remember { mutableStateOf("3") }
    var paxCount by remember { mutableStateOf("0") }
    var cargoWeight by remember { mutableStateOf("0") }
    var fuelLiters by remember { mutableStateOf("2000") }
    var fuelDensity by remember { mutableStateOf("0.8") }

    val crewWeight = (crewCount.toDoubleOrNull() ?: 0.0) * 90.0
    val paxWeight = (paxCount.toDoubleOrNull() ?: 0.0) * 100.0
    val cargo = cargoWeight.toDoubleOrNull() ?: 0.0
    val fuelKg = (fuelLiters.toDoubleOrNull() ?: 0.0) * (fuelDensity.toDoubleOrNull() ?: 0.8)
    
    val totalWeight = (emptyWeight.toDoubleOrNull() ?: 0.0) + crewWeight + paxWeight + cargo + fuelKg
    val maxG = maxWeight.toDoubleOrNull() ?: 13000.0
    val isOverweight = totalWeight > maxG

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Flight Weight & Balance") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "Параметры ВС", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                InputField(value = emptyWeight, onValueChange = { emptyWeight = it }, label = "G пустого (кг)", modifier = Modifier.weight(1f))
                InputField(value = maxWeight, onValueChange = { maxWeight = it }, label = "G макс (кг)", modifier = Modifier.weight(1f))
            }

            Text(text = "Загрузка и экипаж", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                InputField(value = crewCount, onValueChange = { crewCount = it }, label = "Экипаж (чел)", modifier = Modifier.weight(1f))
                InputField(value = paxCount, onValueChange = { paxCount = it }, label = "Пассажиры (чел)", modifier = Modifier.weight(1f))
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                InputField(value = cargoWeight, onValueChange = { cargoWeight = it }, label = "Груз (кг)", modifier = Modifier.weight(1f))
                InputField(value = fuelLiters, onValueChange = { fuelLiters = it }, label = "Топливо (л)", modifier = Modifier.weight(1f))
            }

            InputField(value = fuelDensity, onValueChange = { fuelDensity = it }, label = "Плотность (кг/л)", modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isOverweight) Color(0xFFFFCDD2) else Color(0xFFC8E6C9)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(text = "Результаты расчета", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("Масса топлива: ${"%.1f".format(fuelKg)} кг")
                    Text("Взлетная масса: ${"%.1f".format(totalWeight)} кг / $maxG кг")
                    Text(
                        text = if (isOverweight) "ВНИМАНИЕ: ПЕРЕГРУЗ!" else "Статус: В НОРМЕ",
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isOverweight) Color.Red else Color(0xFF2E7D32),
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun InputField(value: String, onValueChange: (String) -> Unit, label: String, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        modifier = modifier
    )
}
