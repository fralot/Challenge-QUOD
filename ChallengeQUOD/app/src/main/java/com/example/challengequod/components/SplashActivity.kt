package com.example.challengequod.components

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.challengequod.MainActivity
import com.example.challengequod.R

//TELA DE LOADING
class SplashActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            //Animação
            val options = android.app.ActivityOptions.makeCustomAnimation(
                this,
                R.anim.fade_in, // Animação de entrada
                R.anim.fade_out // Animação de saída
            )

            // Inicia a MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent, options.toBundle())

            finish() // Fecha a SplashActivity
        }, 500)
    }

}

