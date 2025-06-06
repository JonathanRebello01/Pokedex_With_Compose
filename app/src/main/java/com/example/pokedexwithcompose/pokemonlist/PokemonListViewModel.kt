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
import com.example.pokedexwithcompose.data.models.PokedexListEntry
import com.example.pokedexwithcompose.data.repository.PokemonRepository
import com.example.pokedexwithcompose.util.Constants.PAGE_SIZE
import com.example.pokedexwithcompose.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
)
    : ViewModel() {

    private var curPage = 0

    var pokemonList = mutableStateOf<List<PokedexListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var cachedPokemonList = listOf<PokedexListEntry>()
    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)

    fun searchPokemonList(query: String){
        val listToSearch = if(isSearchStarting){
        pokemonList.value
        } else{
            cachedPokemonList
        }
        viewModelScope.launch(Dispatchers.Default){
            if (query.isEmpty()){
                pokemonList.value = cachedPokemonList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            val results = listToSearch.filter {
                it.pokemonName.contains(query.trim(), ignoreCase = true) ||
                        it.number.toString() == query.trim()
            }
            if (isSearchStarting){
                cachedPokemonList = pokemonList.value
                isSearchStarting = false
            }
            pokemonList.value = results
            isSearching.value = true
        }
    }

    init {
        loadPokemonPaginated()
    }

    fun loadPokemonPaginated(){
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getPokemonList(PAGE_SIZE, curPage * PAGE_SIZE)
            when(result){
                is Resource.Error<*> -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
                is Resource.Loading<*> -> {
                }
                is Resource.Success<*> -> {

                    endReached.value = curPage * PAGE_SIZE >= result.data!!.count
                    val pokedexEntries = result.data.results.mapIndexed { index, entry ->
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

                    loadError.value = ""
                    isLoading.value = false
                    pokemonList.value += pokedexEntries
                }
            }
        }
    }

    fun calcDominentColor(drawable: SuccessResult, onFinish: (Color) -> Unit) {
        val bmp = drawable.image.toBitmap().copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}