package com.example.challengequod.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.challengequod.R

//CONTEUDO DA TELA INICIAL
@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    onNavigateToService: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar()
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Evoluir inteligência em dados para escolhas inteligentes.",
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Nossos serviços:",
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
                modifier = Modifier.padding(top = 20.dp)
            )

            // Usando ServiceBox para exibir os serviços disponiveis
            ServiceBox(
                title = "Biometria Facial",
                description = "Captura e validação de imagem facial",
                imageRes = R.drawable.biofacial,
                onClick = { onNavigateToService("BioFacialScreen") }
            )
            ServiceBox(
                title = "Biometria Digital",
                description = "Captura e validação de impressão digital",
                imageRes = R.drawable.biodigital,
                onClick = { onNavigateToService("BioDigitalScreen") }
            )
            ServiceBox(
                title = "Documentoscopia",
                description = "Validação e análise de documentos por foto",
                imageRes = R.drawable.documentoscopia,
                onClick = { onNavigateToService("DocumentoScreen") }
            )
            ServiceBox(
                title = "SIM SWAP",
                description = "Verificação de um pedido recente para portabilidade de chip",
                imageRes = R.drawable.simswap,
                onClick = { onNavigateToService("SimSwapScreen") }
            )
            ServiceBox(
                title = "Autenticação Cadastral",
                description = "Validação de informações cadastrais (ex: CPF, telefone, endereço e etc.)",
                imageRes = R.drawable.autenticacao,
                onClick = { onNavigateToService("AutenticaScreen") }
            )
            ServiceBox(
                title = "Score antifraude pessoal",
                description = "Consulta de Score antifraude(1-1000) por meio do CPF",
                imageRes = R.drawable.score,
                onClick = { onNavigateToService("ScoreScreen") }
            )
        }
    }
}

@Composable
fun ServiceBox(
    title: String,
    description: String,
    imageRes: Int? = null,
    backgroundColor: Color = colorResource(id = R.color.CinzaClaro2),
    borderColor: Color = colorResource(id = R.color.RoxoMedio),
    borderWidth: Dp = 1.dp,
    cornerRadius: Dp = 16.dp,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
            .border(
                BorderStroke(borderWidth, borderColor),
                RoundedCornerShape(cornerRadius)
            )
            .padding(8.dp)
            .width(350.dp)
            .clickable(onClick = onClick)

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (imageRes != null) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(70.dp)
                        .padding(end = 8.dp)
                )
            }

            // Coloca título e descrição
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Título
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                // Descrição (texto adicional)
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp
                    )
                )
            }
        }
    }
}








