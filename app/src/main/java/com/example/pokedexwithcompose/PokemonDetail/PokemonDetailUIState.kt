package com.example.pokedexwithcompose.PokemonDetail

import com.plcoding.jetpackcomposepokedex.data.remote.responses.Pokemon

data class PokemonDetailUIState(
    var pokemonWeightInKg: Float = 0f,
    var pokemonHeightInMeters: Float = 0f,
    var animationPlayed: Boolean = false,
    var maxBaseStat: Int = 0
//    var pokemon: Pokemon
)