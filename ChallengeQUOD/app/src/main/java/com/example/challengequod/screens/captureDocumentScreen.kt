package com.example.challengequod.screens

import android.graphics.Bitmap
import android.os.Build
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.challengequod.R
import com.example.challengequod.components.TopBarBack
import com.google.mlkit.vision.face.FaceDetection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

//TELA DE CAPTURA DE IMAGENS DOCUMENTOSCOPIA
@Composable
fun CaptureDocumentScreen(navController: NavController) {
    val context = LocalContext.current

    // Estados para armazenar as imagens capturadas
    var documentImage by remember { mutableStateOf<Bitmap?>(null) }
    var selfieImage by remember { mutableStateOf<Bitmap?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    // Gerenciadores de captura de imagem
    val captureDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            documentImage = bitmap
        } else {
            Toast.makeText(context, "Falha ao capturar a foto do documento.", Toast.LENGTH_SHORT).show()
        }
    }

    val captureSelfieLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            selfieImage = bitmap
        } else {
            Toast.makeText(context, "Falha ao capturar a selfie.", Toast.LENGTH_SHORT).show()
        }
    }

    // Reutiliza função da biometria facial
    fun verifyFaceInSelfie() {
        selfieImage?.let { bitmap ->
            processImageForFaceDetection(
                bitmap = bitmap,
                faceDetector = FaceDetection.getClient(),
                onFacesDetected = { faces ->
                    if (faces.isNotEmpty()) {
                        showSuccessDialog = true
                    } else {
                        showErrorDialog = true
                    }
                }
            )
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
                    text = "Captura de Imagens",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 25.sp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))

                // Instruções
                Text(
                    text = "Capture a foto do documento e uma selfie segurando o documento próximo ao rosto.",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(40.dp))

                // Captura da foto do documento
                Button(
                    onClick = { captureDocumentLauncher.launch() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.RoxoMedio),
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Capturar Documento")
                }

                // Checkmark para o documento
                if (documentImage != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Documento capturado com sucesso",
                            tint = Color.Green,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Documento capturado",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Green)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Captura da selfie com o documento
                Button(
                    onClick = { captureSelfieLauncher.launch() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.RoxoMedio),
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Capturar Selfie com Documento")
                }

                // Checkmark para a selfie
                if (selfieImage != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Selfie capturada com sucesso",
                            tint = Color.Green,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Selfie capturada",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Green)
                        )
                    }
                }
            }

            // Botão na parte inferior para continuar
            Button(
                onClick = {
                    if (documentImage != null && selfieImage != null) {
                        val documentBase64 = bitmapToBase64(documentImage!!)
                        val selfieBase64 = bitmapToBase64(selfieImage!!)
                        val fabricante = Build.MANUFACTURER
                        val modelo = Build.MODEL

                        getCurrentLocation(context) { latitude, longitude ->
                            val jsonBody = """
                {
                  "document": "$documentBase64",
                  "selfie": "$selfieBase64",
                  "metadados": {
                    "fabricante": "$fabricante",
                    "modelo": "$modelo",
                    "latitude": "$latitude",
                    "longitude": "$longitude"
                  }
                }
            """.trimIndent()

                            val client = OkHttpClient()
                            val mediaType = "application/json".toMediaType()
                            val requestBody = jsonBody.toRequestBody(mediaType)

                            val request = Request.Builder()
                                .url("http://10.0.2.2:8080/face/verify")
                                .post(requestBody)
                                .build()

                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val response = client.newCall(request).execute()
                                    val responseBody = response.body?.string()

                                    withContext(Dispatchers.Main) {
                                        if (response.isSuccessful && responseBody?.contains("true") == true) {
                                            showSuccessDialog = true
                                        } else {
                                            showErrorDialog = true
                                        }
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    withContext(Dispatchers.Main) {
                                        showErrorDialog = true
                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Por favor, capture ambas as fotos antes de continuar.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }


                ,
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

            // Popup de Sucesso
            if (showSuccessDialog) {
                AlertDialog(
                    onDismissRequest = { showSuccessDialog = false },
                    title = {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Text(
                                text = "Resultado da documentoscopia",
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
                                    text = "Validação de documentos concluída!",
                                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp)
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                    },
                    text = {
                        Text(
                            text = "Com as fotos capturadas, a validação dos seus documentos foi bem-sucedida.",
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = { showSuccessDialog = false },
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

            // Popup de Erro
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
                                text = "Resultado da documentoscopia",
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
                                    text = "Erro na validação!",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp)
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                    },
                    text = {
                        Text(
                            text = "O rosto extraído da selfie não corresponde ao do documento ou não foi possível detectá-lo.",
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

    fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

}







