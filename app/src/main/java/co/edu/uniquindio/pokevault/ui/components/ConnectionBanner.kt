package co.edu.uniquindio.pokevault.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConnectionBanner(isOnline: Boolean) {
    AnimatedVisibility(visible = !isOnline) {
        Box(
            modifier = Modifier.fillMaxWidth().background(Color(0xFFD32F2F)).padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Sin conexión a Internet — Modo Offline", color = Color.White, fontSize = 12.sp)
        }
    }
}