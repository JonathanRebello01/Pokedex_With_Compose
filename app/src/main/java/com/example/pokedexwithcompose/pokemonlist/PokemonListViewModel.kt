package com.example.pokedexwithcompose.pokemonlist

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import coil3.request.SuccessResult
import coil3.toBitmap
import com.example.pokedexwithcompose.data.CurrentPokemonRepositoryImpl
import com.example.pokedexwithcompose.data.models.PokedexListEntry
import com.example.pokedexwithcompose.data.repository.PokemonRepository
import com.example.pokedexwithcompose.domain.pokedex.repositories.CurrentyPokemonRepository
import com.example.pokedexwithcompose.pokemondetail.PokemonDetailUIState
import com.example.pokedexwithcompose.util.Constants.PAGE_SIZE
import com.example.pokedexwithcompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository,
    val CurrentyRepository: CurrentPokemonRepositoryImpl

): ViewModel() {

    fun cleanRepository(){
        CurrentyRepository.clearData()
    }



    private val _uiState = MutableStateFlow(PokemonListUIState(
        textSearcBar = "",
        isHintDisplayed = true,
        pokemonList = emptyList<PokedexListEntry>(),
        endReached = false,
        loadError = "",
        isLoading = false,
        isSearching = false,
        dominantColor = Color.Unspecified
    ))

    val uiState: StateFlow<PokemonListUIState> = _uiState.asStateFlow()

    private var curPage = 0

    private var cachedPokemonList: List<PokedexListEntry>? = listOf<PokedexListEntry>()
    private var isSearchStarting = true





    fun searchPokemonList(query: String){
        val listToSearch = if(isSearchStarting){
        uiState.value.pokemonList
        } else{
            cachedPokemonList
        }
        viewModelScope.launch(Dispatchers.Default){

            _uiState.update { state ->
                state.copy(
                    textSearcBar = query,
                    isHintDisplayed = query.isEmpty()
                )
            }


            if (query.isEmpty()){
                _uiState.update { state ->
                    state.copy(
                        isHintDisplayed = true,
                        pokemonList = cachedPokemonList,
                        isSearching = false
                    )
                }
                isSearchStarting = true
                return@launch
            } else{
                _uiState.update { state ->
                    state.copy(
                        isHintDisplayed = false
                    )
                }
            }
            val results = listToSearch?.filter {
                it.pokemonName.contains(query.trim(), ignoreCase = true) ||
                        it.number.toString() == query.trim()
            }
            if (isSearchStarting){
                cachedPokemonList = uiState.value.pokemonList
                isSearchStarting = false
            }

            _uiState.update { state ->
                state.copy(
                    isSearching = true,
                    pokemonList = results
                )
            }


        }
    }

    init {
        loadPokemonPaginated()
    }

    fun loadPokemonPaginated(){
        viewModelScope.launch {

            _uiState.update { state ->
                state.copy(
                    isLoading = true
                )
            }

            val result = repository.getPokemonList(PAGE_SIZE, curPage * PAGE_SIZE)
            when(result){
                is Resource.Error<*> -> {

                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            loadError =  result.message!!
                        )
                    }
                }
                is Resource.Loading<*> -> {
                }
                is Resource.Success<*> -> {
                    _uiState.update { state ->
                        state.copy(
                            endReached = curPage * PAGE_SIZE >= result.data!!.count
                        )
                    }

                    val pokedexEntries = result.data?.results?.mapIndexed { index, entry ->
                        val number = if(entry.url.endsWith("/")){
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        }
                        else{
                            entry.url.takeLastWhile { it.isDigit() }
                        }

                        val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                        Log.d("ImageURL", "URL: $url")
                        Log.d("pokemonName", "Name: ${entry.name.capitalize(Locale.current)}")
                        PokedexListEntry(entry.name.capitalize(Locale.current) , url, number.toInt())
                    }
                    curPage++

                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            loadError = "",
                            pokemonList = state.pokemonList?.plus((pokedexEntries ?: emptyList()))

                        )
                    }
                }
            }
        }
    }

    fun calcDominentColor(drawable: SuccessResult, onFinish: (Color) -> Unit) {
        val bmp = drawable.image.toBitmap().copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(
                    Color(colorValue)
                )
//                _uiState.update { state ->
//                    state.copy(
//                        dominantColor = Color(colorValue)
//                    )
//                }
            }

        }
    }

    fun cleanDominantColor(){
        _uiState.update { state ->
            state.copy(
                dominantColor = Color.Unspecified
            )
        }
    }

}

