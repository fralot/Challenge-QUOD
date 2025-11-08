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
import androidx.compose.material.icons.filled.CheckCircle
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

//TELA DE AUTENTICAÇÃO CADASTRAL
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuntenticaScreen(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("Resultado da Autenticação") }
    var dialogMessage by remember { mutableStateOf("") }
    var dialogSubtitle by remember { mutableStateOf("") }
    var dialogIcon by remember { mutableStateOf<@Composable (() -> Unit)?>(null) }

    // Estados para os campos de entrada
    var cpf by remember { mutableStateOf("") }
    var nome by remember { mutableStateOf("") }
    var endereco by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }

    // Funções de validação simples
    fun isCpfValid(cpf: String): Boolean {
        // Validar CPF (exemplo simplificado)
        return cpf.length == 11 && cpf.all { it.isDigit() }
    }

    fun isPhoneValid(telefone: String): Boolean {
        // Validar telefone (exemplo simplificado)
        return telefone.length == 11 && telefone.all { it.isDigit() }
    }

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
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "Autenticação Cadastral",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 25.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "Digite suas informações cadastrais para que possamos validá-las :",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Campos de entrada (CPF, Nome, Endereço, Telefone)
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

                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome completo") },
                    placeholder = { Text("Digite seu nome completo",
                        style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Gray.copy(alpha = 0.8f)
                    )) },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorResource(id = R.color.RoxoMedio),
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = colorResource(id = R.color.black),
                        focusedLabelColor = colorResource(id = R.color.RoxoMedio)
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = endereco,
                    onValueChange = { endereco = it },
                    label = { Text("Endereço") },
                    placeholder = { Text("Digite seu endereço",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.Gray.copy(alpha = 0.8f)
                        )) },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colorResource(id = R.color.RoxoMedio),
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = colorResource(id = R.color.black),
                        focusedLabelColor = colorResource(id = R.color.RoxoMedio)
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = telefone,
                    onValueChange = { telefone = it },
                    label = { Text("Telefone Celular (com DDD)") },
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

                // Esse Spacer vai fazer a coluna ocupar o máximo de espaço
                Spacer(modifier = Modifier.weight(1f)) // Garantir que o conteúdo acima ocupe o espaço restante

            }

            Button(
                onClick = {
                    // Lista de conjuntos de informações válidas
                    val validEntries = listOf(
                        mapOf(
                            "cpf" to "12345678901",
                            "nome" to "João da Silva",
                            "endereco" to "Rua das Flores, 123",
                            "telefone" to "11987654321"
                        ),
                        mapOf(
                            "cpf" to "98765432100",
                            "nome" to "Maria Oliveira",
                            "endereco" to "Avenida Central, 456",
                            "telefone" to "21987654321"
                        ),
                        mapOf(
                            "cpf" to "11223344556",
                            "nome" to "Carlos Souza",
                            "endereco" to "Travessa Alegre, 789",
                            "telefone" to "31987654321"
                        )
                    )

                    // Verificar se os dados inseridos correspondem a algum conjunto válido
                    val isValid = validEntries.any { entry ->
                        entry["cpf"] == cpf &&
                                entry["nome"] == nome &&
                                entry["endereco"] == endereco &&
                                entry["telefone"] == telefone
                    }

                    if (isValid) {
                        dialogMessage = "Autenticado com sucesso"
                        dialogSubtitle = "Informações cadastrais correspondem aos registros!"
                        dialogIcon = {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Sucesso",
                                tint = Color.Green
                            )
                        }
                    } else {
                        dialogMessage = "Erro na autenticação"
                        dialogSubtitle = when {
                            cpf.isEmpty() || nome.isEmpty() || endereco.isEmpty() || telefone.isEmpty() ->
                                "Nenhum campo pode estar vazio."
                            else -> "As informações fornecidas não correspondem aos registros."
                        }
                        dialogIcon = {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Erro",
                                tint = Color.Red
                            )
                        }
                    }

                    dialogTitle = "Resultado da autenticação"
                    showDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.RoxoMedio),
                    contentColor = Color.White
                )
            ) {
                Text("Enviar")
            }


            // Popup com AlertDialog
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = {
                        Text(
                            dialogTitle,
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp)
                        )
                    },
                    text = {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                dialogIcon?.invoke()

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    dialogMessage,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp,
                                        color = Color.Black)
                                )
                            }

                            Spacer(modifier = Modifier.height(15.dp))

                            if (dialogSubtitle.isNotEmpty()) {
                                Text(
                                    dialogSubtitle,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                                )
                            }
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











