package com.example.pokedexwithcompose.data

import androidx.compose.ui.graphics.Color
import com.example.pokedexwithcompose.data.local.entities.PokemonEntity
import com.example.pokedexwithcompose.domain.pokedex.repositories.CurrentyPokemonRepository
import com.example.pokedexwithcompose.pokemonlist.PokemonList
import com.plcoding.jetpackcomposepokedex.data.remote.responses.Pokemon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CurrentPokemonRepositoryImpl @Inject constructor() : CurrentyPokemonRepository {

    private var _currentPokemonData = MutableStateFlow(PokemonEntity())
    override val currentPokemonData: StateFlow<PokemonEntity> = _currentPokemonData

    override var dominantColor: Color = Color.Unspecified

    override val pokemonName: String?
        get() = _currentPokemonData.value.pokemonName


    fun setPokemon(pokemon: PokemonEntity) {
        _currentPokemonData.value = pokemon
    }

    fun getPokemon(): PokemonEntity {
        return _currentPokemonData.value
    }

    fun clearData() {
        _currentPokemonData.value = PokemonEntity()
    }

}
