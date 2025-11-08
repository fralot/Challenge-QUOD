package com.example.challengequod

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.challengequod.components.MainContent
import com.example.challengequod.screens.AuntenticaScreen
import com.example.challengequod.screens.BioDigitalScreen
import com.example.challengequod.screens.BioFacialScreen
import com.example.challengequod.screens.CaptureDocumentScreen
import com.example.challengequod.screens.CaptureFaceScreen
import com.example.challengequod.screens.DocumentoScreen
import com.example.challengequod.screens.ScoreScreen
import com.example.challengequod.screens.SimSwapScreen
import com.example.challengequod.ui.theme.ChallengeQUODTheme
import android.Manifest

class MainActivity : ComponentActivity() {

    private lateinit var cameraPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var locationPermissionLauncher: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializando o launcher para solicitar permissões
        cameraPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Permissão concedida!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permissão de câmera é necessária para continuar.", Toast.LENGTH_SHORT).show()
            }
        }

        locationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Permissão de localização concedida!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permissão de localização é necessária para detectar fraudes.", Toast.LENGTH_LONG).show()
            }
        }

        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)


        enableEdgeToEdge()

        setContent {
            ChallengeQUODTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "main",
                ) {
                    composable("main") {
                        MainContent(
                            onNavigateToService = { route ->
                                navController.navigate(route)
                            }
                        )
                    }

                    composable("ScoreScreen") {
                        ScoreScreen(navController)
                    }

                    composable("AutenticaScreen") {
                        AuntenticaScreen(navController)
                    }

                    composable("SimSwapScreen") {
                        SimSwapScreen(navController)
                    }

                    composable("BioFacialScreen") {
                        BioFacialScreen(navController)
                    }

                    composable("CaptureFaceScreen") {
                        CaptureFaceScreen(navController)
                    }

                    composable("BioDigitalScreen"){
                        BioDigitalScreen(navController)
                    }

                    composable("DocumentoScreen"){
                        DocumentoScreen(navController)
                    }

                    composable("captureDocumentScreen"){
                        CaptureDocumentScreen(navController)
                    }


                }
            }
        }
    }
}








