package com.example.pokedexwithcompose.pokemonlist

import android.util.Log
import androidx.compose.foundation.Image
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import com.example.pokedexwithcompose.R
import com.example.pokedexwithcompose.data.models.PokedexListEntry
import com.plcoding.jetpackcomposepokedex.ui.theme.RobotoCondensed

@Composable
fun PokemonListScreen(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
){
    Surface (
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ){
        Column (modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp)){
            Image(
                painter = painterResource(id = R.drawable.ic_international_pok_mon_logo),
                contentDescription = "Pokemon",
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .align(CenterHorizontally)
            )
            SearchBar(Modifier
                .fillMaxWidth()
                .padding(16.dp), "Search...")
            {
                viewModel.searchPokemonList(it)
            }
            PokemonList(navController = navController)
        }
    }

}

@Composable
fun SearchBar(modifier: Modifier = Modifier, hint: String = "", onSearch: (String) -> Unit = {}){
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }
    Box(modifier = modifier){
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = !it.isFocused && text.isNotEmpty()
                }
        )

        if(isHintDisplayed){
            Text(text = hint, color = Color.LightGray, modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp))
        }
    }
}

@Composable
fun PokemonList(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
){
    val pokemonList by remember { viewModel.pokemonList }
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }
    val isSearching by remember { viewModel.isSearching }

    LazyColumn (
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.padding(top = 16.dp)
        ) {
        val itemCount = if(pokemonList.size % 2 == 0) {
            pokemonList.size / 2
        }
        else{
            pokemonList.size / 2 + 1
        }
        items(itemCount){
            if (it >= itemCount -1 && !endReached && !isLoading && !isSearching){
                    viewModel.loadPokemonPaginated()
            }
            PokedexRow(rowIndex = it, entries = pokemonList, navController = navController)
        }
    }
}



@Composable
fun PokedexEntry(
    entry: PokedexListEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = hiltViewModel()
){
    val defaultDominantColor = MaterialTheme.colorScheme.surface
    var dominantColor by remember { mutableStateOf(defaultDominantColor) }
    val context = LocalContext.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(1f))
            .background(
                Brush.verticalGradient(
                    listOf(dominantColor, defaultDominantColor)
                )
            )
            .clickable {

                Log.i("Teste", "${dominantColor.toArgb()}/${entry.pokemonName}")

                navController.navigate(

                    "pokemon_detail_screen/${dominantColor.toArgb()}/${entry.pokemonName}"


                )
            }

    ){
        Column (

        ){

            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
            contentAlignment = Alignment.Center
            ){
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(entry.imageUrl)
                        .allowHardware(false)
                        .crossfade(true)
                        .listener(
                            onStart = { Log.d("ImageDebug", "Start loading") },
                            onSuccess = { _, result ->
                                Log.d("ImageDebug", "Image loaded")
                                println("Result: $result")
                                viewModel.calcDominentColor(result){
                                        color -> dominantColor = color
                                }},
                            onError = { _, result ->
                                Log.e("ImageDebug", "Error: ${result.throwable}") }
                        )
                        .build(),
                    contentDescription = entry.pokemonName,
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.Fit
                )
                if (viewModel.isLoading.value){
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.scale(0.5f)
                    )
                }
            }

            Text(
                text = entry.pokemonName,
                fontFamily = RobotoCondensed,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
fun PokedexRow(
    rowIndex: Int,
    entries: List<PokedexListEntry>,
    navController: NavController
){
    Column {
        Row{
            PokedexEntry(
                entry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(16.dp))
            if(entries.size >= rowIndex * 2+2){
                PokedexEntry(
                    entry = entries[rowIndex * 2+1],
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            }
            else{
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}