package com.example.challengequod.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.challengequod.R
import com.example.challengequod.components.CameraManager
import com.example.challengequod.components.TopBarBack
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.io.ByteArrayOutputStream
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.time.Instant


//TELA DE CAPUTRA FACIAL BIOMETRIA FACIAL
@Composable
fun CaptureFaceScreen(navController: NavController) {
    var facesDetected by remember { mutableStateOf<List<Face>>(emptyList()) }
    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val faceDetector = remember { FaceDetection.getClient() }
    val lifecycleOwner = LocalLifecycleOwner.current


    val previewView = remember { PreviewView(context) }
    val imageCapture = remember { ImageCapture.Builder().build() }
    val cameraSelector = remember { CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build() }

    val cameraManager = remember {
        CameraManager(
            context = context,
            lifecycleOwner = lifecycleOwner,
            previewView = previewView,
            imageCapture = imageCapture,
            cameraSelector = cameraSelector
        )
    }

    LaunchedEffect(cameraManager) {
        cameraManager.startCamera()
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
            AndroidView(
                factory = { previewView.apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                } },
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {}

            Button(
                onClick = {
                    cameraManager.captureImage { bitmap ->
                        capturedImage = bitmap
                        if (bitmap != null) {
                            processImageForFaceDetection(bitmap, faceDetector) { faces ->
                                facesDetected = faces
                                if (faces.isNotEmpty()) {
                                    val base64Image = bitmapToBase64(bitmap)
                                    val fabricante = Build.MANUFACTURER
                                    val modelo = Build.MODEL

                                    // Recuperar localização antes de enviar
                                    getCurrentLocation(context) { latitude, longitude ->
                                        sendFaceImageToBackend(
                                            base64Image = base64Image,
                                            fabricante = fabricante,
                                            modelo = modelo,
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

                                } else {
                                    showErrorDialog = true
                                }
                            }
                        } else {
                            showErrorDialog = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .align(Alignment.BottomCenter),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EE),
                    contentColor = Color.White
                )
            ) {
                Text(text = "Capturar Imagem")
            }
        }

        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                title = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Resultado da biometria", style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp))
                        Spacer(modifier = Modifier.height(15.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Ícone de Sucesso", tint = Color.Green, modifier = Modifier.size(24.dp).padding(end = 8.dp))
                            Text("Biometria facial concluída!", style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp))
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                },
                text = {
                    Text("Com base na foto capturada, sua biometria facial foi escaneada e validada.",
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

        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                title = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Resultado da biometria", style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp))
                        Spacer(modifier = Modifier.height(15.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = "Ícone de Erro", tint = Color.Red, modifier = Modifier.size(24.dp).padding(end = 8.dp))
                            Text("Erro na biometria!", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp))
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                },
                text = {
                    Text("Nenhuma face detectada ou face incompatível com os dados.",
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


fun processImageForFaceDetection(
    bitmap: Bitmap,
    faceDetector: FaceDetector,
    onFacesDetected: (List<Face>) -> Unit
) {
    val inputImage = InputImage.fromBitmap(bitmap, 0)

    val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE) // Melhor precisão
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL) // Detecta todos os marcos faciais
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL) // Detecta emoções e direção da face
        .build()

    val faceDetectorWithOptions = FaceDetection.getClient(options)

    faceDetectorWithOptions.process(inputImage)
        .addOnSuccessListener { faces ->
            onFacesDetected(faces)
        }
        .addOnFailureListener { exception ->
            onFacesDetected(emptyList()) // Nenhuma face detectada
            Log.e("FaceDetection", "Erro na detecção de faces: ${exception.message}")
        }
}

fun bitmapToBase64(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    val byteArray = outputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun sendFaceImageToBackend(
    base64Image: String,
    fabricante: String,
    modelo: String,
    latitude: Double?,
    longitude: Double?,
    onSuccess: () -> Unit,
    onError: () -> Unit
) {
    val client = OkHttpClient()

    val dataCaptura = Instant.now().toString() // formato ISO 8601

    val metadados = """
        "metadados": {
            "fabricante": "$fabricante",
            "modelo": "$modelo",
            "dataCaptura": "$dataCaptura",
            "latitude": ${latitude ?: "null"},
            "longitude": ${longitude ?: "null"}
        }
    """.trimIndent()

    val json = """
        {
            "imagem": "$base64Image",
            "tipoBiometria": "facial",
            $metadados
        }
    """.trimIndent()

    val requestBody = json.toRequestBody("application/json".toMediaType())
    val request = Request.Builder()
        .url("http://10.0.2.2:8080/api/biometria/processar")
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onError()
        }

        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string()

            if (response.isSuccessful && responseBody != null) {
                try {
                    val jsonObject = JSONObject(responseBody)
                    val status = jsonObject.getString("status")

                    if (status == "SUCESSO") {
                        onSuccess()
                    } else if (status == "FRAUDE") {
                        onError() // ou você pode abrir outro diálogo especial pra fraude
                    } else {
                        onError()
                    }
                } catch (e: Exception) {
                    onError()
                }
            } else {
                onError()
            }
        }

    })
}

fun getCurrentLocation(
    context: Context,
    onLocationReceived: (latitude: Double?, longitude: Double?) -> Unit
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
    ) {
        onLocationReceived(null, null)
        return
    }

    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        if (location != null) {
            onLocationReceived(location.latitude, location.longitude)
        } else {
            // Tentar uma nova leitura
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 1000L
            ).setMaxUpdates(1).build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    val loc = result.lastLocation
                    if (loc != null) {
                        onLocationReceived(loc.latitude, loc.longitude)
                    } else {
                        onLocationReceived(null, null)
                    }
                    fusedLocationClient.removeLocationUpdates(this)
                }
            }

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
    }.addOnFailureListener {
        onLocationReceived(null, null)
    }
}


