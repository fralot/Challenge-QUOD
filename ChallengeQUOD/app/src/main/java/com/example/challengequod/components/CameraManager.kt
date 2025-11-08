package com.example.challengequod.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.io.File

//GERENCIA A CAMERA
class CameraManager(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val previewView: PreviewView,
    private val imageCapture: ImageCapture,
    private val cameraSelector: CameraSelector
) {
    private lateinit var cameraProvider: ProcessCameraProvider

    // Método para iniciar a câmera
    fun startCamera() {
        // Obtém o cameraProvider
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(context))
    }

    // Método para vincular os casos de uso da câmera ao ciclo de vida
    private fun bindCameraUseCases() {
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            Preview.Builder().build().apply { setSurfaceProvider(previewView.surfaceProvider) },
            imageCapture
        )
    }

    // Método para capturar uma imagem
    fun captureImage(onImageCaptured: (Bitmap?) -> Unit) {
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(File(context.filesDir, "capturedImage.jpg")).build()
        imageCapture.takePicture(
            outputFileOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val imagePath = File(context.filesDir, "capturedImage.jpg")
                    val bitmap = BitmapFactory.decodeFile(imagePath.absolutePath)
                    onImageCaptured(bitmap)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraManager", "Erro ao capturar imagem: ${exception.message}")
                    onImageCaptured(null)
                }
            }
        )
    }
}


