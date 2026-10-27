package co.edu.uniquindio.pokevault.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.uniquindio.pokevault.domain.repository.PokemonRepository
import co.edu.uniquindio.pokevault.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListViewModel @Inject constructor(
    private val repository: PokemonRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(ListUiState())
    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()

    init {
        loadNextPage()
    }

    fun onSearchQueryChanged(newQuery: String) {
        _uiState.update { it.copy(searchQuery = newQuery, currentPage = 1, pokemons = emptyList(), canLoadMore = true) }
        loadNextPage()
    }

    fun onTypeFilterChanged(newType: String) {
        _uiState.update { it.copy(selectedType = newType, currentPage = 1, pokemons = emptyList(), canLoadMore = true) }
        loadNextPage()
    }

    fun loadNextPage() {
        if(_uiState.value.isLoadingPage || !_uiState.value.canLoadMore) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingPage = true) }
            val state = _uiState.value
            when (val result = repository.getPokemonList(state.currentPage, state.searchQuery, state.selectedType)){
                is Resource.Success -> {
                    val newItems = result.data
                    _uiState.update {
                        it.copy(
                            pokemons = it.pokemons + newItems,
                            isLoadingPage = false,
                            currentPage = it.currentPage + 1,
                            canLoadMore = newItems.isNotEmpty() && state.searchQuery.isBlank() && state.selectedType.isBlank()
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoadingPage = false, error = result.message) }
                }
                else -> {}
            }
        }
    }
}
