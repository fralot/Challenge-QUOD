package com.example.challengequod.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.challengequod.R
import com.example.challengequod.components.TopBarBack

//TELA DE SCORE ANTIFRAUDE
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreScreen(navController: NavController) {
    var cpf by remember { mutableStateOf("") } // Armazena o CPF
    var isCpfChecked by remember { mutableStateOf(false) } // Controla a validação
    var score by remember { mutableStateOf(0) } // Armazena o score baseado no CPF
    val animatedScore = animateFloatAsState(targetValue = score.toFloat()) // Animação do score

    Scaffold(
        topBar = {
            TopBarBack(onBackClick = { navController.popBackStack() })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título
                Text(
                    text = "Consultar Score Antifraude",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 25.sp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(40.dp))

                // Descrição
                Text(
                    text = "Digite o seu CPF para consultar o score antifraude:",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Campo de texto para CPF
                OutlinedTextField(
                    value = cpf,
                    onValueChange = { newCpf -> cpf = newCpf },
                    label = { Text("CPF") },
                    placeholder = { Text("ex: 12345678901",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.Gray.copy(alpha = 0.8f)
                        )) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorResource(id = R.color.RoxoMedio),
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = colorResource(id = R.color.black),
                        focusedLabelColor = colorResource(id = R.color.RoxoMedio)
                    )
                )

                // Mensagem de erro (exibida somente se o CPF for inválido após clicar no botão)
                if (isCpfChecked && !isCpfValid(cpf)) {
                    Text(
                        text = "CPF inválido",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Exibe o termômetro somente se o CPF for válido
                if (score > 0) {
                    Spacer(modifier = Modifier.height(50.dp))

                    Text(
                        text = "Score do CPF consultado:",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 22.sp),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Número do Score com fundo destacado
                    Text(
                        text = score.toString(),
                        color = Color.Black,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 30.sp),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .background(
                                calculateScoreColor(score),
                                shape = RoundedCornerShape(8.dp)
                            ) // Fundo com cor baseada no score
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Termômetro horizontal com gradiente de cores
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.LightGray)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(animatedScore.value / 1000f) // Progresso proporcional ao score
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(12.dp))
                                .background(calculateScoreColor(score)) // Cor baseada no score
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Mensagem personalizada com fundo destacado
                    Text(
                        text = getScoreMessage(score),
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .background(
                                calculateScoreColor(score),
                                shape = RoundedCornerShape(8.dp)
                            ) // Fundo com cor baseada no score
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }


            }

            // Botão na parte inferior
            Button(
                onClick = {
                    isCpfChecked = true // Marca que o CPF deve ser validado
                    if (isCpfValid(cpf)) {
                        val lastDigit = cpf.last().digitToInt() // Obtém o último dígito
                        score = generateScore(lastDigit) // Gera o score com base no último dígito
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.RoxoMedio)
                )
            ) {
                Text("Consultar Score")
            }
        }
    }
}

// Função para gerar o score
fun generateScore(lastDigit: Int): Int {
    if (lastDigit == 0) {
        return 1000 // Caso especial para o último dígito 0
    }
    val randomPart = (10..99).random() // Gera os dois últimos dígitos aleatórios
    return lastDigit * 100 + randomPart // Combina o último dígito com os aleatórios
}


// Calcula a cor baseada no score (gradiente entre vermelho, amarelo e verde)
@Composable
fun calculateScoreColor(score: Int): Color {
    val red = Color(0xFFFF0000) // Cor para score baixo
    val yellow = Color(0xFFFFFF00) // Cor intermediária
    val green = Color(0xFF00FF00) // Cor para score alto

    return when (score) {
        in 0..500 -> lerp(red, yellow, score / 500f) // Transição de vermelho para amarelo
        in 501..1000 -> lerp(yellow, green, (score - 500) / 500f) // Transição de amarelo para verde
        else -> green // Caso limite
    }
}

// Função que retorna a mensagem personalizada com base no intervalo do score
fun getScoreMessage(score: Int): String {
    return when (score) {
        in 0..200 -> "Grande chance de fraude."
        in 201..400 -> "Risco moderado de fraude."
        in 401..600 -> "Risco baixo de fraude."
        in 601..800 -> "Confiabilidade alta."
        in 801..1000 -> "Confiabilidade excelente."
        else -> "Score inválido." // Caso para valores inesperados
    }
}

// Função simples para validar se o CPF tem o formato correto
fun isCpfValid(cpf: String): Boolean {
    return cpf.length == 11 && cpf.all { it.isDigit() }
}






