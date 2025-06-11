package com.plcoding.jetpackcomposepokedex.di

import com.example.pokedexwithcompose.data.pokedex.CurrentPokemonRepositoryImpl
import com.example.pokedexwithcompose.data.pokedex.remote.PokeApi
import com.example.pokedexwithcompose.data.pokedex.PokemonRepository
import com.example.pokedexwithcompose.domain.pokedex.repositories.CurrentyPokemonRepository
import com.example.pokedexwithcompose.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CurrentPokemonRepositoryDataModule {

    @Singleton
    @Provides
    fun providePokemonRepository(
        api: PokeApi
    ) = PokemonRepository(api)

    @Singleton
    @Provides
    fun providePokeApi(): PokeApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PokeApi::class.java)

    }


    @Singleton
    @Provides
    fun provideCurrentPokemonRepository(): CurrentyPokemonRepository {
    return CurrentPokemonRepositoryImpl()
    }


}