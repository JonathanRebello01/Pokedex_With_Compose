package com.example.pokedexwithcompose.ui.screens.pokemonlist

import androidx.compose.ui.graphics.Color
import com.example.pokedexwithcompose.data.models.PokedexListEntry
import kotlinx.coroutines.flow.StateFlow

data class PokemonListUIState (
    var textSearcBar: String = "",
    var isHintDisplayed: Boolean = true,
    var pokemonList: List<PokedexListEntry>?,
    var endReached: Boolean = false,
    var loadError: String = "",
    var isLoading: Boolean = false,
    var isSearching: Boolean = false,
    var dominantColor: Color = Color.Unspecified,
)