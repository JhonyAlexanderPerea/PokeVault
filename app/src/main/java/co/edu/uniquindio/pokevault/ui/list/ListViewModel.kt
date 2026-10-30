package co.edu.uniquindio.pokevault.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.uniquindio.pokevault.domain.repository.PokemonRepository
import co.edu.uniquindio.pokevault.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListUiState())
    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null
    private var prefetchJob: Job? = null
    private var requestGeneration = 0

    init {
        loadNextPage()
    }

    fun onSearchQueryChanged(newQuery: String) {
        if (newQuery == _uiState.value.searchQuery) return
        resetAndLoad { it.copy(searchQuery = newQuery) }
    }

    fun onTypeFilterChanged(newType: String) {
        if (newType == _uiState.value.selectedType) return
        resetAndLoad { it.copy(selectedType = newType) }
    }

    fun retry() {
        _uiState.update { it.copy(error = null) }
        loadNextPage()
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (state.isLoadingPage || !state.canLoadMore) return

        val generation = requestGeneration
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoadingPage = true, error = null) }

            // Si ya hay una precarga en curso para esta misma página, esperamos
            // a que termine para reutilizar la caché del repositorio.
            prefetchJob?.join()
            if (generation != requestGeneration) return@launch

            val current = _uiState.value
            when (val result = repository.getPokemonList(current.currentPage, current.searchQuery, current.selectedType)) {
                is Resource.Success -> {
                    if (generation != requestGeneration) return@launch
                    val page = result.data
                    val existingIds = current.pokemons.mapTo(HashSet()) { it.id }
                    val newItems = page.pokemons.filter { existingIds.add(it.id) }

                    _uiState.update {
                        it.copy(
                            pokemons = it.pokemons + newItems,
                            isLoadingPage = false,
                            currentPage = it.currentPage + 1,
                            canLoadMore = page.hasMore && newItems.isNotEmpty(),
                            error = null
                        )
                    }

                    if (_uiState.value.canLoadMore) prefetchNextPage()
                }
                is Resource.Error -> {
                    if (generation != requestGeneration) return@launch
                    _uiState.update { it.copy(isLoadingPage = false, error = result.message) }
                }
                else -> {}
            }
        }
    }

    private fun prefetchNextPage() {
        if (prefetchJob?.isActive == true) return
        val state = _uiState.value
        if (!state.canLoadMore) return

        prefetchJob = viewModelScope.launch {
            repository.getPokemonList(state.currentPage, state.searchQuery, state.selectedType)
        }
    }

    private fun resetAndLoad(transform: (ListUiState) -> ListUiState) {
        requestGeneration++
        loadJob?.cancel()
        prefetchJob?.cancel()

        _uiState.update {
            transform(it).copy(
                pokemons = emptyList(),
                currentPage = 1,
                canLoadMore = true,
                isLoadingPage = false,
                error = null
            )
        }

        loadNextPage()
    }
}
