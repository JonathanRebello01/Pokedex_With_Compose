package com.example.pokedexwithcompose.pokemondetail

import android.util.Log
import androidx.compose.animation.core.AnimationState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexwithcompose.data.repository.PokemonRepository
import com.example.pokedexwithcompose.util.Resource
import com.plcoding.jetpackcomposepokedex.data.remote.responses.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PokemonDetailUIState(animationPlayed = false, maxBaseStat =  0, resourcePokemon = null))
    val uiState: StateFlow<PokemonDetailUIState> = _uiState.asStateFlow()

    fun setAnimationPlayed(value: Boolean) {
        _uiState.update { state ->
            state.copy(
                animationPlayed = value
            )
        }
    }

    fun getPokemonInfo(pokemonName: String) {

        viewModelScope.launch (){
            val result = repository.getPokemonInfo(pokemonName)

            when(result){
            is Resource.Success -> {
                Log.i("TesteViewModel", "Sucesso")
                _uiState.update { state ->
                    state.copy(
                        resourcePokemon = result,
                    )
                }
            }

                is Resource.Error<*> -> {

                    Log.i("TesteViewModel", "Erro")

                    _uiState.update { state ->
                        state.copy(
                            resourcePokemon = result,
                        )
                    }
                }
                is Resource.Loading<*> -> {
                    Log.i("TesteViewModel", "Loading")
                    _uiState.update { state ->
                        state.copy(
                            resourcePokemon = null,
                        )
                    }
                }
            }
        }
    }
}
