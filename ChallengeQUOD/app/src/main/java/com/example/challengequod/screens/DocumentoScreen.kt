package com.example.challengequod.screens

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.challengequod.R
import com.example.challengequod.components.TopBarBack

//TELA INCIAL DOCUMENTOSCOPIA
@Composable
fun DocumentoScreen(navController: NavController) {
    val context = LocalContext.current
    context as Activity

    // Gerenciador de permissões
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Se a permissão for concedida, navega para a próxima tela
            navController.navigate("captureDocumentScreen")
        } else {
            // Exibe uma mensagem caso a permissão seja negada
            Toast.makeText(context, "Permissão para usar a câmera é necessária.", Toast.LENGTH_LONG).show()
        }
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
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título
                Text(
                    text = "Documentoscopia",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 25.sp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(40.dp))

                // Descrição
                Text(
                    text = "Leia atentamente as instruções abaixo antes de capturar as imagens necessárias:",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                )
                Spacer(modifier = Modifier.height(30.dp))


                // Lista de instruções
                val documentoscopiaInstructions = listOf(
                    "Prepare seu documento de identificação válido.",
                    "Certifique-se de que o ambiente esteja bem iluminado.",
                    "Tire uma foto do documento, garantindo que todos os detalhes estejam visíveis.",
                    "Tire uma selfie segurando o documento próximo ao rosto.",
                    "Evite usar acessórios como óculos ou chapéu para maior precisão na análise."
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {

                    documentoscopiaInstructions.forEach { instruction ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Instrução",
                                tint = colorResource(id = R.color.RoxoMedio),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = instruction,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                                textAlign = TextAlign.Start,
                                modifier = Modifier.wrapContentWidth()
                            )
                        }
                    }
                }

            }

            // Botão na parte inferior
            Button(
                onClick = {
                    // Verifica se a permissão já foi concedida
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        // Permissão concedida, navega para a próxima tela
                        navController.navigate("captureDocumentScreen")
                    } else {
                        // Solicita a permissão de câmera
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.RoxoMedio),
                    contentColor = Color.White
                )
            ) {
                Text(text = "Continuar")
            }
        }
    }
}


