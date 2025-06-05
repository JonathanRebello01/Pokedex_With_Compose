package com.example.pokedexwithcompose.pokemonlist

import androidx.compose.ui.graphics.Color
import com.example.pokedexwithcompose.data.models.PokedexListEntry

data class PokemonListUIState (
    var textSearchBar: String,
    var isHintDisplayed: Boolean,
    val pokemonList: List<PokedexListEntry>,
    val endReached: Boolean,
    val loadError: Boolean,
    val isLoading: Boolean,
    val isSearching: Boolean,
    val dominantColor: Color

)