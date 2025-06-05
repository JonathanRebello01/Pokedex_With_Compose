package com.example.pokedexwithcompose.pokemondetail

data class PokemonDetailUIState(
    var pokemonWeightInKg: Float = 0f,
    var pokemonHeightInMeters: Float = 0f,
    var animationPlayed: Boolean = false,
    var maxBaseStat: Int = 0
//    var pokemon: Pokemon
)