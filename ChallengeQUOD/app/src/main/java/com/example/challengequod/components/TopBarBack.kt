package com.example.challengequod.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.challengequod.R

//TOPBAR COM BOTÃO DE VOLTAR
@Composable
fun TopBarBack(
    onBackClick: () -> Unit,

) {
    Column(
        modifier = Modifier.padding(top = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(Color.White)

        ) {
            // Botão de voltar no lado esquerdo
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.Black
                )
            }

            // Imagem centralizada
            Image(
                painter = painterResource(id = R.drawable.maxresdefault22),
                contentDescription = "Logo QuOD",
                modifier = Modifier
                    .size(125.dp)
                    .align(Alignment.Center)
            )
        }

        // Barra inferior com degradê
        Box(
            modifier = Modifier
                .height(4.dp)
                .width(300.dp)
                .align(Alignment.CenterHorizontally)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            colorResource(id = R.color.VerdeClaro),
                            colorResource(id = R.color.RoxoClaro)
                        ),
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
        )
    }
}

