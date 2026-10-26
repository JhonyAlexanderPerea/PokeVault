package co.edu.uniquindio.pokevault.domain.model

data class Pokemon (
    val id: Int,
    val title: String,
    val imageUrl: String,
    val types: List<String> = emptyList()
)