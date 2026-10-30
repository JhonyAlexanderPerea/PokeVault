package co.edu.uniquindio.pokevault.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private enum class ConnectionStatus(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val gradient: List<Color>
) {
    Offline(
        icon = Icons.Rounded.WifiOff,
        title = "Sin conexión a Internet",
        subtitle = "Modo offline activado",
        gradient = listOf(Color(0xFFE53935), Color(0xFFB71C1C))
    ),
    Restored(
        icon = Icons.Rounded.Wifi,
        title = "¡Conexión restablecida!",
        subtitle = "Ya estás de nuevo en línea",
        gradient = listOf(Color(0xFF43A047), Color(0xFF1B5E20))
    )
}

@Composable
fun ConnectionBanner(
    isOnline: Boolean,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf(ConnectionStatus.Offline) }
    // Solo mostramos "restablecida" si antes hubo una desconexión real
    var wasOffline by remember { mutableStateOf(false) }

    LaunchedEffect(isOnline) {
        if (!isOnline) {
            wasOffline = true
            status = ConnectionStatus.Offline
            visible = true
        } else if (wasOffline) {
            status = ConnectionStatus.Restored
            visible = true
            delay(3000)
            visible = false
            wasOffline = false
        }
    }

    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = slideInVertically(tween(400)) { -it } + fadeIn(tween(400)),
        exit = slideOutVertically(tween(400)) { -it } + fadeOut(tween(400))
    ) {
        AnimatedContent(
            targetState = status,
            transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
            label = "connection_banner_content"
        ) { current ->
            val shape = RoundedCornerShape(16.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .shadow(8.dp, shape)
                    .clip(shape)
                    .background(Brush.horizontalGradient(current.gradient))
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = current.icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(
                        text = current.title,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = current.subtitle,
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}