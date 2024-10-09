package com.example.my_calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculatorUI()
        }
    }
}

@Composable
fun CalculatorUI() {
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<String?>(null) }

    fun calculate(expression: String): String {
        val operators = arrayOf("+", "-", "×", "÷")
        val numbers = expression.split(" ").filter { it.isNotEmpty() }
        var total = 0.0
        var currentOperator = "+"

        for (number in numbers) {
            if (operators.contains(number)) {
                currentOperator = number
            } else {
                val num = number.toDoubleOrNull()
                if (num != null) {
                    total = when (currentOperator) {
                        "+" -> total + num
                        "-" -> total - num
                        "×" -> total * num
                        "÷" -> if (num != 0.0) total / num else Double.NaN
                        else -> total
                    }
                }
            }
        }
        return total.toString()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Parte gris para mostrar el resultado
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            // Resultado en la parte superior
            Text(
                text = result ?: "",
                fontSize = 48.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                color = Color.Black
            )

            // Campo de entrada
            BasicTextField(
                value = input,
                onValueChange = { input = it },
                textStyle = TextStyle(fontSize = 36.sp, textAlign = TextAlign.End),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(16.dp)
            )
        }

        // Sección de botones
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val buttonModifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(4.dp)

            // Filas de botones
            val rows = listOf(
                listOf("C", "÷", "×", "⌫"),
                listOf("7", "8", "9", "-"),
                listOf("4", "5", "6", "+"),
                listOf("1", "2", "3", "."),
                listOf("0", "=")
            )

            // Crear los botones directamente en las filas
            for (row in rows) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                ) {
                    for (item in row) {
                        Button(
                            onClick = {
                                when (item) {
                                    "+" -> input += " + "
                                    "-" -> input += " - "
                                    "×" -> input += " × "
                                    "÷" -> input += " ÷ "
                                    "C" -> {
                                        input = ""
                                        result = null
                                    }
                                    "=" -> {
                                        // Evaluar la expresión y mostrar el resultado
                                        result = calculate(input)
                                    }
                                    //Al oprimir el boton se tienen en cuenta los espacios entre los numeros y el operador
                                    "⌫" -> {
                                        if (input.isNotEmpty()) input = input.dropLast(1)
                                    }
                                    else -> input += item
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffb97fff)), // Cambiar el color del botón
                            modifier = buttonModifier
                        ) {
                            Text(text = item, fontSize = 24.sp)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreviewUI() {
    CalculatorUI()
}