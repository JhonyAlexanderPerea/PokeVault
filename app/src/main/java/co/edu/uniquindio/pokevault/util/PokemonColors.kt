package co.edu.uniquindio.pokevault.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

object PokemonColors {
    private val DarkSurface = Color(0xFF1C1B1F)

    fun getTypeColor(type: String, darkTheme: Boolean = false): Color {
        val base = baseTypeColor(type)
        return if (darkTheme) lerp(base, Color.White, 0.35f) else base
    }

    fun getTypeCardBackground(type: String, darkTheme: Boolean = false): Color {
        return if (darkTheme) {
            lerp(baseTypeColor(type), DarkSurface, 0.82f)
        } else {
            lightTypeCardBackground(type)
        }
    }

    private fun baseTypeColor(type: String): Color {
        return when (type.lowercase().trim()) {
            "fire", "fuego" -> Color(0xFFFF7F50)
            "water", "agua" -> Color(0xFF4A90E2)
            "grass", "planta", "hierba" -> Color(0xFF52B788)
            "electric", "electrico", "eléctrico" -> Color(0xFFFFC107)
            "poison", "veneno" -> Color(0xFFA040A0)
            "bug", "bicho" -> Color(0xFF8B9A1B)
            "normal" -> Color(0xFFA8A878)
            "ground", "tierra" -> Color(0xFFD4A373)
            "fairy", "hada" -> Color(0xFFF4ACB7)
            "fighting", "lucha" -> Color(0xFFD62828)
            "psychic", "psiquico", "psíquico" -> Color(0xFFF72585)
            "rock", "roca" -> Color(0xFFB8A038)
            "ghost", "fantasma" -> Color(0xFF705898)
            "ice", "hielo" -> Color(0xFF74C69D)
            "dragon", "dragón" -> Color(0xFF6C5CE7)
            "dark", "siniestro" -> Color(0xFF5D4037)
            "steel", "acero" -> Color(0xFF90A4AE)
            "flying", "volador" -> Color(0xFFA890F0)
            else -> Color(0xFFA8A878)
        }
    }

    private fun lightTypeCardBackground(type: String): Color {
        return when (type.lowercase().trim()) {
            "fire", "fuego" -> Color(0xFFFFE5D9)
            "water", "agua" -> Color(0xFFE3F2FD)
            "grass", "planta", "hierba" -> Color(0xFFE8F5E9)
            "electric", "electrico", "eléctrico" -> Color(0xFFFFFDE7)
            "poison", "veneno" -> Color(0xFFF3E5F5)
            "bug", "bicho" -> Color(0xFFF1F8E9)
            "normal" -> Color(0xFFF5F5F5)
            "ground", "tierra" -> Color(0xFFFFF8E1)
            "fairy", "hada" -> Color(0xFFFCE4EC)
            "fighting", "lucha" -> Color(0xFFFFEBEE)
            "psychic", "psiquico", "psíquico" -> Color(0xFFFCE4EC)
            "rock", "roca" -> Color(0xFFFFF3E0)
            "ghost", "fantasma" -> Color(0xFFEDE7F6)
            "ice", "hielo" -> Color(0xFFE0F7FA)
            "dragon", "dragón" -> Color(0xFFEDE7F6)
            "dark", "siniestro" -> Color(0xFFEFEBE9)
            "steel", "acero" -> Color(0xFFECEFF1)
            "flying", "volador" -> Color(0xFFEDE7F6)
            else -> Color(0xFFF5F5F5)
        }
    }
}
