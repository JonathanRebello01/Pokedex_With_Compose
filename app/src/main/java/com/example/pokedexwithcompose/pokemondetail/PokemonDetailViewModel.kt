package com.example.pokedexwithcompose.pokemondetail

import androidx.lifecycle.ViewModel
import com.example.pokedexwithcompose.repository.PokemonRepository
import com.example.pokedexwithcompose.util.Resource
import com.plcoding.jetpackcomposepokedex.data.remote.responses.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PokemonDetailUIState())
    val uiState: StateFlow<PokemonDetailUIState> = _uiState.asStateFlow()

    fun setPokemonWeightInKg(pokemonWeight: Int){
        _uiState.update { state ->
            state.copy(
                pokemonWeightInKg = (round(pokemonWeight * 100f) / 1000f)
            )
        }
    }

    fun setPokemonHeightInM(pokemonHeight: Int) {
        _uiState.update { state ->
            state.copy(
                pokemonHeightInMeters = round(pokemonHeight * 100f) / 1000f
            )
        }
    }
    fun setAnimationPlayed(value: Boolean) {
        _uiState.update { state ->
            state.copy(
                animationPlayed = value
            )
        }
    }
    fun setMaxBaseStat(pokemonInfo: Pokemon) {
        _uiState.update { state ->
            state.copy(
                maxBaseStat = pokemonInfo.stats.maxOf { it.base_stat }
            )
        }
    }

    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {
//        trata chamada
//                sucs   state.pokem = data
//                errore  state.error = message
//                when(pokemonInfo) {
//                    is Resource.Success -> {
//                    }
//                }: Resource<Pokemon>
        return repository.getPokemonInfo(pokemonName)
    }
}
