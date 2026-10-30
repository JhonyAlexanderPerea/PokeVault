package co.edu.uniquindio.pokevault.ui.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import co.edu.uniquindio.pokevault.ui.components.TypeBadge
import co.edu.uniquindio.pokevault.ui.theme.LocalPokeVaultDarkTheme
import co.edu.uniquindio.pokevault.util.PokemonColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    uiState: DetailUiState,
    onRetry: () -> Unit,
    onBackClick: () -> Unit
) {
    BackHandler {
        onBackClick()
    }

    val pokemon = uiState.pokemon
    val darkTheme = LocalPokeVaultDarkTheme.current
    val primaryType = pokemon?.types?.firstOrNull() ?: "normal"
    val themeColor = PokemonColors.getTypeColor(primaryType, darkTheme)
    val bgCardColor = PokemonColors.getTypeCardBackground(primaryType, darkTheme)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(pokemon?.name ?: "Detalle del Pokémon") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = bgCardColor
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(bgCardColor)
        ) {
            if (uiState.isLoading && pokemon == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = themeColor)
            } else if (uiState.error != null && pokemon == null) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = "Error al cargar el detalle",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = uiState.error,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Button(onClick = onRetry) {
                        Text("Reintentar")
                    }
                }
            } else pokemon?.let { p ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header Artwork Box
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = if (darkTheme) 0.1f else 0.7f)),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = p.imageUrl,
                            contentDescription = p.name,
                            modifier = Modifier.size(160.dp)
                        )
                    }

                    // ID & Name
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "#${p.id.toString().padStart(3, '0')}",
                            style = MaterialTheme.typography.titleMedium,
                            color = themeColor,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = p.name,
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Types
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        p.types.forEach { type ->
                            TypeBadge(type = type)
                        }
                    }

                    // Physical attributes card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Altura", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("${p.height / 10.0} m", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            }
                            VerticalDivider(modifier = Modifier.height(32.dp))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Peso", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("${p.weight / 10.0} kg", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Description card with language toggle
                    var selectedLang by remember { mutableStateOf("ES") }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Descripción",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = themeColor,
                                    fontWeight = FontWeight.Bold
                                )
                                
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    FilterChip(
                                        selected = selectedLang == "ES",
                                        onClick = { selectedLang = "ES" },
                                        label = { Text("ES") }
                                    )
                                    FilterChip(
                                        selected = selectedLang == "EN",
                                        onClick = { selectedLang = "EN" },
                                        label = { Text("EN") }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (selectedLang == "ES") p.descriptionEs else p.descriptionEn,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    // Stats card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Estadísticas Base",
                                style = MaterialTheme.typography.titleMedium,
                                color = themeColor,
                                fontWeight = FontWeight.Bold
                            )
                            
                            StatBar(label = "HP", value = p.hp, max = 255, color = themeColor)
                            StatBar(label = "Ataque", value = p.attack, max = 200, color = themeColor)
                            StatBar(label = "Defensa", value = p.defense, max = 230, color = themeColor)
                            StatBar(label = "Velocidad", value = p.speed, max = 200, color = themeColor)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatBar(label: String, value: Int, max: Int, color: Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Text(text = "$value", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = color)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { (value / max.toFloat()).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = color.copy(alpha = 0.2f)
        )
    }
}
