package com.example.pokedexwithcompose.domain.pokedex.entities

import androidx.compose.ui.graphics.Color
import androidx.room.Entity


data class PokemonEntity (
    var dominantColor: Color = Color.Companion.Unspecified,
    var pokemonName: String = ""
)