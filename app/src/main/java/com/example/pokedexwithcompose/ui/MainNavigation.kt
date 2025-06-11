package com.example.pokedexwithcompose.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pokedexwithcompose.ui.screens.pokemondetail.PokemonDetailScreen
import com.example.pokedexwithcompose.ui.screens.pokemonlist.PokemonListScreen
import com.plcoding.jetpackcomposepokedex.ui.theme.JetpackComposePokedexTheme

@Composable
fun MainNavigation(){
    val navController = rememberNavController()
//    val context = LocalContext.current

//    fun exitApplication() {
//        val activity = context as? Activity
//        activity?.finish()
//    }

    JetpackComposePokedexTheme(){
        NavHost(
            navController = navController,
            startDestination = Destinations.POKEMON_LIST_SCREEN
        ){
            composable(Destinations.POKEMON_LIST_SCREEN){
                PokemonListScreen(
                    navController = navController,
                    )

            }
            composable(
                Destinations.POKEMON_DETAIL_SCREEN
//                                "/{dominantColor}/{pokemonName}"
//                        arguments = listOf(
//                            navArgument("dominantColor") {
//                                type = NavType.IntType
//                            },
//                            navArgument("pokemonName") {
//                                type = NavType.StringType
//                            }),
            ){
//                        val dominantColor = remember {
//
//                            val color = it.arguments?.getInt("dominantColor")
//                            color?.let { Color(it) } ?: Color.Unspecified
//                        }
//                        val pokemonName = remember {
//                            it.arguments?.getString("pokemonName")
//                        }
                PokemonDetailScreen(
//                            dominantColor = dominantColor,
//                            pokemonName = pokemonName?.lowercase(Locale.ROOT) ?: "",
                    navController = navController
                )
            }
        }
    }


}

object Destinations {
    const val POKEMON_LIST_SCREEN = "pokemon_list_screen"
    const val POKEMON_DETAIL_SCREEN = "pokemon_detail_screen"
}

