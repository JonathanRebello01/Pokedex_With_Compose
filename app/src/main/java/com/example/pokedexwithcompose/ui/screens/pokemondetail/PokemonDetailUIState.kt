package com.example.pokedexwithcompose.ui.screens.pokemondetail

import com.example.pokedexwithcompose.util.Resource
import com.plcoding.jetpackcomposepokedex.data.remote.responses.Pokemon

data class PokemonDetailUIState(
    var animationPlayed: Boolean = false,
    var maxBaseStat: Int? = 0,
    var resourcePokemon: Resource<Pokemon>?,
)