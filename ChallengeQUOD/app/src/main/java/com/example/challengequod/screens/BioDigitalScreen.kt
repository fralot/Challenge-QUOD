package com.example.challengequod.screens

import android.os.Build
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import com.example.challengequod.R
import com.example.challengequod.components.TopBarBack
import kotlinx.coroutines.delay
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException
import java.time.Instant

//TELA DE BIOMETRIA DIGITAL
@Composable
fun BioDigitalScreen(navController: NavController) {
    var showErrorDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var isScanning by remember { mutableStateOf(false) }

    val context = LocalContext.current
    var errorCount by remember { mutableStateOf(0) }


    // Controle da animação
    val scanPosition by animateFloatAsState(
        targetValue = if (isScanning) 1f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
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
                    text = "Biometria Digital",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 25.sp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Texto descritivo
                Text(
                    text = "Pressione o botão abaixo para capturar sua biometria digital.",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 22.sp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Botão com clique e comportamento de pressão longa
                Box(
                    modifier = Modifier
                        .size(250.dp)
                        .background(colorResource(id = R.color.RoxoMedio), shape = CircleShape)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    showErrorDialog = true // Exibir popup de erro para toque curto
                                },
                                onLongPress = {
                                    // Não precisa de lógica aqui pois usaremos o tempo no onPress
                                },
                                onPress = {
                                    val pressStartTime = System.currentTimeMillis()
                                    isScanning = true

                                    val wasReleased = try {
                                        awaitRelease()
                                        true
                                    } catch (e: Exception) {
                                        false
                                    }

                                    val pressDuration = System.currentTimeMillis() - pressStartTime
                                    isScanning = false

                                    if (wasReleased && pressDuration >= 2000) {
                                        errorCount = 0 // reseta erros após sucesso

                                        getCurrentLocation(context) { latitude, longitude ->
                                            sendDigitalBiometryToBackend(
                                                fabricante = Build.MANUFACTURER,
                                                modelo = Build.MODEL,
                                                latitude = latitude,
                                                longitude = longitude,
                                                onSuccess = {
                                                    showSuccessDialog = true
                                                },
                                                onError = {
                                                    showErrorDialog = true
                                                }
                                            )
                                        }
                                    }

                                    else {
                                        // Pressionado por tempo insuficiente → "fraude"
                                        showErrorDialog = true
                                        errorCount++

                                        if (errorCount >= 4) {
                                            getCurrentLocation(context) { latitude, longitude ->
                                                sendFraudeToBackend(
                                                    fabricante = Build.MANUFACTURER,
                                                    modelo = Build.MODEL,
                                                    latitude = latitude,
                                                    longitude = longitude
                                                )
                                            }
                                            errorCount = 0 // zera o contador após envio de fraude
                                        }
                                    }


                                }
                            )
                        }
                    ,
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Imagem
                        Image(
                            painter = painterResource(id = R.drawable.fingerprintscan),
                            contentDescription = "Ícone de Biometria",
                            modifier = Modifier
                                .size(175.dp)
                                .align(Alignment.Center)
                        )

                        // Animação de scan
                        if (isScanning) {
                            Canvas(
                                modifier = Modifier
                                    .size(175.dp)
                                    .align(Alignment.Center)
                            ) {
                                val scanHeight = size.height * 0.1f
                                val yOffset = lerp(0f, size.height - scanHeight, scanPosition)

                                drawRect(
                                    color = Color.White.copy(alpha = 0.8f),
                                    topLeft = Offset(0f, yOffset),
                                    size = Size(size.width, scanHeight)
                                )
                            }
                        }
                    }
                }

                // Instruções abaixo do botão
                Spacer(modifier = Modifier.height(70.dp))
                val fingerprintInstructions = listOf(
                    "Coloque seu dedo sobre o sensor (preferencialmente o polegar).",
                    "Mantenha o dedo pressionado até o escaneamento ser concluído.",
                    "Caso haja erro, tente novamente."
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(15.dp)

                ) {
                    Text(
                        text = "Instruções:",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )


                    fingerprintInstructions.forEach { instruction ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
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
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                }

            }

            // Popups
            if (showSuccessDialog) {
                AlertDialog(
                    onDismissRequest = { /* Nenhuma ação ao tentar fechar com toque fora */ },
                    title = {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Resultado da biometria",
                                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(15.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Ícone de Sucesso",
                                    tint = Color.Green,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .padding(end = 8.dp)
                                )
                                Text(
                                    text = "Biometria digital concluída!",
                                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp)
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                    },
                    text = {
                        Text(
                            text = "Sua biometria digital foi escaneada e validada com sucesso.",
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = { showSuccessDialog = false; isScanning = false },
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

            if (showErrorDialog) {
                AlertDialog(
                    onDismissRequest = { showErrorDialog = false },
                    title = {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Resultado da biometria",
                                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(15.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Ícone de Erro",
                                    tint = Color.Red,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .padding(end = 8.dp)
                                )
                                Text(
                                    text = "Erro na biometria!",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp)
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                    },
                    text = {
                        Text(
                            text = "Não foi possível capturar sua biometria digital. Tente novamente.",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = { showErrorDialog = false },
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

fun sendDigitalBiometryToBackend(
    fabricante: String,
    modelo: String,
    latitude: Double?,
    longitude: Double?,
    onSuccess: () -> Unit,
    onError: () -> Unit
) {
    val client = OkHttpClient()

    val dataCaptura = Instant.now().toString() // formato ISO 8601

    val json = """
        {
            "tipoBiometria": "digital",
            "metadados": {
                "fabricante": "$fabricante",
                "modelo": "$modelo",
                "dataCaptura": "$dataCaptura",
                "latitude": ${latitude ?: "null"},
                "longitude": ${longitude ?: "null"}
            }
        }
    """.trimIndent()

    val requestBody = json.toRequestBody("application/json".toMediaType())
    val request = Request.Builder()
        .url("http://10.0.2.2:8080/api/biometria/processar") // ajuste a URL conforme necessário
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onError()
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                onSuccess()
            } else {
                onError()
            }
        }
    })
}

fun sendFraudeToBackend(
    fabricante: String,
    modelo: String,
    latitude: Double?,
    longitude: Double?
) {
    val client = OkHttpClient()
    val dataCaptura = Instant.now().toString()

    val json = """
        {
            "tipoBiometria": "digital",
            "metadados": {
                "fabricante": "$fabricante",
                "modelo": "$modelo",
                "dataCaptura": "$dataCaptura",
                "latitude": ${latitude ?: "null"},
                "longitude": ${longitude ?: "null"},
                "fraude": true
            }
        }
    """.trimIndent()

    val requestBody = json.toRequestBody("application/json".toMediaType())
    val request = Request.Builder()
        .url("http://10.0.2.2:8080/api/biometria/processar")
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {}
        override fun onResponse(call: Call, response: Response) {}
    })
}










