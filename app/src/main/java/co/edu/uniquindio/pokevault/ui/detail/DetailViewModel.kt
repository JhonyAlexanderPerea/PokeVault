package co.edu.uniquindio.pokevault.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.uniquindio.pokevault.domain.repository.PokemonRepository
import co.edu.uniquindio.pokevault.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: PokemonRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        savedStateHandle.get<String>("pokemonId")?.let { id ->
            loadDetail(id)
        }
    }

    private fun loadDetail(id: String) {
        viewModelScope.launch {
            _uiState.value = DetailUiState(isLoading = true)
            when (val result = repository.getPokemonDetail(id)) {
                is Resource.Success -> _uiState.value = DetailUiState(pokemon = result.data)
                is Resource.Error -> _uiState.value = DetailUiState(error = result.message)
                else -> {}
            }
        }
    }
}