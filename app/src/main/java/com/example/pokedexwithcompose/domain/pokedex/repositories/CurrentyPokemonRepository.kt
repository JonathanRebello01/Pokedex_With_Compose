package com.example.pokedexwithcompose.domain.pokedex.repositories

import androidx.compose.ui.graphics.Color
import com.example.pokedexwithcompose.domain.pokedex.entities.PokemonEntity
import kotlinx.coroutines.flow.StateFlow

interface CurrentyPokemonRepository {
    val currentPokemonData: StateFlow<PokemonEntity>
    val dominantColor: Color?
    val pokemonName: String?
}