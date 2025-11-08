package com.example.challengequod.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.challengequod.R
import com.example.challengequod.components.TopBarBack

//TELA DE SIMSWAP
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimSwapScreen(navController: NavController) {
    // Estados para os inputs
    var cpf by remember { mutableStateOf("") }
    var numeroCelular by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("Resultado") }
    var dialogMessage by remember { mutableStateOf("") }
    var dialogIcon by remember { mutableStateOf<@Composable (() -> Unit)?>(null) }

    // Lista de combinações de CPF e número de celular que teram alerta de portabilidade
    val blockedCombinations = listOf(
        "12345678901" to "11234567890",
        "98765432100" to "11987654321",
        "11122233344" to "11999999999"
    )

    Scaffold(
        topBar = {
            TopBarBack(onBackClick = { navController.popBackStack() })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Campos de entrada e título
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .align(Alignment.TopStart),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título
                Text(
                    text = "Sim Swap",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 25.sp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "Digite o CPF e o número vinculado ao seu cadastro na operadora telefônica:",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Campo CPF
                OutlinedTextField(
                    value = cpf,
                    onValueChange = { cpf = it },
                    label = { Text("CPF") },
                    placeholder = { Text("ex: 12345678901",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.Gray.copy(alpha = 0.8f)
                        )) },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorResource(id = R.color.RoxoMedio),
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = colorResource(id = R.color.black),
                        focusedLabelColor = colorResource(id = R.color.RoxoMedio)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Número de Celular
                OutlinedTextField(
                    value = numeroCelular,
                    onValueChange = { numeroCelular = it },
                    label = { Text("Número de Celular (com DDD)") },
                    placeholder = { Text("ex: 11234567890",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.Gray.copy(alpha = 0.8f)
                        )) },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorResource(id = R.color.RoxoMedio),
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = colorResource(id = R.color.black),
                        focusedLabelColor = colorResource(id = R.color.RoxoMedio)
                    )
                )
            }

            // Botão de enviar na parte inferior
            Button(
                onClick = {
                    when {
                        cpf.isEmpty() || numeroCelular.isEmpty() -> {
                            dialogTitle = "Erro de verificação"
                            dialogMessage = "Todos os campos são obrigatórios!"
                            dialogIcon = {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Erro",
                                    tint = Color.Red
                                )
                            }
                        }
                        !cpf.matches(Regex("\\d{11}")) -> {
                            dialogTitle = "Erro de verificação"
                            dialogMessage = "O CPF deve conter 11 números."
                            dialogIcon = {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Erro",
                                    tint = Color.Red
                                )
                            }
                        }
                        !numeroCelular.matches(Regex("\\d{11}")) -> {
                            dialogTitle = "Erro de verificação"
                            dialogMessage = "O número de celular deve conter 11 números (incluindo DDD)."
                            dialogIcon = {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Erro",
                                    tint = Color.Red
                                )
                            }
                        }
                        blockedCombinations.contains(cpf to numeroCelular) -> {
                            dialogTitle = "Troca confirmada"
                            dialogMessage = "Foi detectada uma solicitação recente de portabilidade de chip relacionada a estes dados!"
                            dialogIcon = {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Informação",
                                    tint = Color.Red
                                )
                            }
                        }
                        else -> {
                            dialogTitle = "Sem Atividade"
                            dialogMessage = "Nenhuma atividade recente foi detectada para estas informações."
                            dialogIcon = {}
                        }
                    }
                    showDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .align(Alignment.BottomCenter),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.RoxoMedio),
                    contentColor = Color.White
                )
            ) {
                Text("Enviar")
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Título principal
                            Text(
                                text = "Resultado da autenticação",
                                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(15.dp))

                            // Título secundário (atual)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                dialogIcon?.invoke()
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    dialogTitle,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp)
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                    },
                    text = {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Mensagem principal
                            Text(
                                text = dialogMessage,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                            )

                            Spacer(modifier = Modifier.height(15.dp))

                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = { showDialog = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.RoxoMedio),
                                contentColor = Color.White
                            )
                        ) {
                            Text("OK")
                        }
                    }
                )
            }

        }
    }
}




