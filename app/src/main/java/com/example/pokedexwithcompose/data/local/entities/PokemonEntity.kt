package com.example.pokedexwithcompose.data.local.entities

import androidx.compose.ui.graphics.Color
import androidx.room.Entity

@Entity
data class PokemonEntity (
    var dominantColor: Color = Color.Unspecified,
    var pokemonName: String = ""
)

