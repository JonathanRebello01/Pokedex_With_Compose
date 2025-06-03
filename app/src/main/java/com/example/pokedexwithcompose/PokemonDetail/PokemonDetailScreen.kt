package com.example.pokedexwithcompose.PokemonDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.pokedexwithcompose.util.Resource
import com.example.pokedexwithcompose.util.parseTypeToColor
import com.plcoding.jetpackcomposepokedex.data.remote.responses.Pokemon
import com.plcoding.jetpackcomposepokedex.data.remote.responses.Type
import java.util.Locale

@Composable
fun PokemonDetailScreen(
    dominantColor: Color,
    pokemonName: String,
    navController: NavController,
    topPaddind: Dp = 20.dp,
    pokemonImageSize: Dp = 200.dp,
    viewModel: PokemonDetailViewModel = hiltViewModel()

){
    val context = LocalContext.current

    val pokemonInfo = produceState<Resource<Pokemon>>(initialValue = Resource.Loading()) {
        value = viewModel.getPokemonInfo(pokemonName)
    }.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColor)
            .padding(bottom = 16.dp)
    ){
        pokemonDetailTopSection(
            navControler = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .align(Alignment.TopCenter)
        )
        PokemonDetailStateWrapper(
            pokemonInfo = pokemonInfo,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPaddind + pokemonImageSize / 2f, start = 16.dp, end = 16.dp, bottom = 16.dp)
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
                .align(Alignment.BottomCenter),
                loadingModifier = Modifier
                    .size(100.dp)
                    .align(Alignment.Center)
                    .padding(top = topPaddind + pokemonImageSize / 2f, start = 16.dp, end = 16.dp, bottom = 16.dp)

        )
        Box(contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
        ){
            if (pokemonInfo is Resource.Success){
                pokemonInfo.data?.sprites?.let(){
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(it.front_default)
                            .build(),
                        contentDescription = pokemonInfo.data.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(pokemonImageSize)
                            .offset(y = topPaddind)
                    )
                }

            }
        }
    }
}

@Composable
fun pokemonDetailTopSection(
    navControler: NavController,
    modifier: Modifier = Modifier
){
    Box(contentAlignment = Alignment.TopStart,
        modifier = modifier
            .background(Brush.verticalGradient(
                listOf(
                    Color.Black,
                    Color.Transparent
                )
            ))
    ){
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .padding(top = 20.dp)
                .size(36.dp)
                .offset(16.dp, 16.dp)
                .clickable{
                    navControler.popBackStack()
                }
        )
    }
}

@Composable
fun PokemonDetailStateWrapper(
    pokemonInfo: Resource<Pokemon>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier
){
    when(pokemonInfo) {
        is Resource.Success -> {

        }

        is Resource.Error -> {
            Text(
                text = pokemonInfo.message!!,
                color = Color.Red,
                modifier = modifier
            )

        }
        is Resource.Loading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = loadingModifier
            )

        }
    }
}

@Composable
fun PokemonDetailSection(
    pokemonInfo: Pokemon,
    modifier: Modifier = Modifier
){
    val scrollState = rememberScrollState()
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(100.dp)
            .verticalScroll(scrollState)
        ){
        Text(
            text = "# ${pokemonInfo.id} ${pokemonInfo.name.capitalize(Locale.ROOT)}",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun PokemonTypeSction(
    types: List<Type>
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
    ){

        for (type in types){
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .clip(CircleShape)
                    .background(parseTypeToColor(type))
                    .height(35.dp)
                    .padding(horizontal = 8.dp)

            ){
                Text(
                  text = type.type.name.capitalize(Locale.ROOT),
                  color = Color.White,
                  fontSize = 18.sp
                )
            }
        }

    }
}

@Composable
fun PokemonDetailDataSection(){
    
}

