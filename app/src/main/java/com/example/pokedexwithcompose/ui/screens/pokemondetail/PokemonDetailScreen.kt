package com.example.pokedexwithcompose.ui.screens.pokemondetail

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.pokedexwithcompose.R
import com.example.pokedexwithcompose.util.Resource
import com.example.pokedexwithcompose.util.parseStatToAbbr
import com.example.pokedexwithcompose.util.parseStatToColor
import com.example.pokedexwithcompose.util.parseTypeToColor
import com.plcoding.jetpackcomposepokedex.data.remote.responses.Pokemon
import com.plcoding.jetpackcomposepokedex.data.remote.responses.Type
import java.util.Locale

@Composable
fun PokemonDetailScreen(
    viewModel: PokemonDetailViewModel = hiltViewModel(),
    navController: NavController,
    topPaddind: Dp = 20.dp,
    pokemonImageSize: Dp = 200.dp,
){
    val dominantColor: Color = viewModel.currentyRepository.dominantColor
    val pokemonName: String = viewModel.currentyRepository.getPokemon().pokemonName
//    viewModel.cleanRepository()


    val context = LocalContext.current
    var state: PokemonDetailUIState = viewModel.uiState.collectAsState().value

        viewModel.getPokemonInfo(pokemonName)


    var pokemonInfo = state.resourcePokemon

    when (val result = state.resourcePokemon) {
        is Resource.Success -> {

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
                    pokemonInfo = pokemonInfo?.data,
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
        is Resource.Error -> {

            Log.i("Teste", "Erro")

            Text(
                text = "Erro ao carregar dados: ${result.message ?: "desconhecido"}",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )

        }
        null, is Resource.Loading -> {

            Log.i("Teste", "Loading")

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
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
                .size(36.dp)
                .offset(x = 16.dp, y = 50.dp)
                .clickable {
                    navControler.popBackStack()
                }
        )
    }
}

@Composable
fun PokemonDetailStateWrapper(
    pokemonInfo: Pokemon?,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier
){

            PokemonDetailSection(
                pokemonInfo = pokemonInfo,
                modifier = modifier
                    .offset(y = ((-20).dp))
            )

        if(pokemonInfo == null) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = loadingModifier
            )
        }
}

@Composable
fun PokemonDetailSection(
    pokemonInfo: Pokemon?,
    modifier: Modifier = Modifier
){
    val scrollState = rememberScrollState()
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(y = 100.dp)
            .verticalScroll(scrollState)
        ){
        Text(
            text = "# ${pokemonInfo?.id} ${pokemonInfo?.name?.capitalize(Locale.ROOT)}",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
        PokemonTypeSction(types = pokemonInfo?.types)
        PokemonDetailDataSection(pokemonInfo)
        PokemonBasicStats(pokemonInfo = pokemonInfo)
    }
}

@Composable
fun PokemonTypeSction(
    types: List<Type>?
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
    ){

        if (types != null) {
            for (type in types){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .clip(CircleShape)
                        .background(parseTypeToColor(type))
                        .height(35.dp)


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
}

@Composable
fun PokemonDetailDataSection(
    pokemonInfo: Pokemon?,
    sectionHeight: Dp = 80.dp
){

    Row(
        modifier = Modifier.
        fillMaxWidth()
    ){
        PokemonDetailDataItem(dataValue = (pokemonInfo?.weight?.times(100f)?.div(1000f)),
            dataUnit = "kg",
            dataIcon = painterResource(id = R.drawable.ic_weight),
            modifiter = Modifier.weight(1f)
        )
        Spacer(
            modifier = Modifier
                .size(1.dp, sectionHeight)
                .background(Color.LightGray)
        )
        PokemonDetailDataItem(dataValue = (pokemonInfo?.height?.times(100f)?.div(1000f)),
            dataUnit = "m",
            dataIcon = painterResource(id = R.drawable.ic_height),
            modifiter = Modifier.weight(1f)
        )

    }

}

@Composable
fun PokemonDetailDataItem(
    dataValue: Float?,
    dataUnit: String,
    dataIcon: Painter,
    modifiter: Modifier = Modifier
){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifiter
    )
    {
        Icon(painter = dataIcon,  contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "${dataValue}${dataUnit}",
            color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun PokemonStats(
    statName: String,
    statValue: Int,
    statMaxValue: Number,
    statColor: Color,
    heigth: Dp = 28.dp,
    animDuration: Int = 1000,
    animDelay: Int =0
){

    val viewModel: PokemonDetailViewModel = hiltViewModel()
    val state = viewModel.uiState.collectAsState().value

    val curPercent = animateFloatAsState(
        targetValue = if(state.animationPlayed){
            statValue / statMaxValue.toFloat()
        } else 0f,
        animationSpec = tween(animDuration, animDelay)
    )

    LaunchedEffect(key1 = true) {
        if (!state.animationPlayed) {
        viewModel.setAnimationPlayed(true)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(heigth)
            .clip(CircleShape)
            .background(
                if (isSystemInDarkTheme()) Color(0xFF505050)
                else Color.LightGray
            )
    ){
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(curPercent.value)
                .clip(CircleShape)
                .background(statColor)
                .padding(horizontal = 8.dp)
        ){
            Text(
                text = statName,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = (curPercent.value * statMaxValue.toInt()).toInt().toString(),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PokemonBasicStats(
    pokemonInfo: Pokemon?,
    animDelatPerItem: Int = 100
){
    val viewModel: PokemonDetailViewModel = hiltViewModel()
    var state = viewModel.uiState.collectAsState().value


    var maxBaseStats: Int? = pokemonInfo?.stats?.maxOf { it.base_stat }
//    viewModel.setMaxBaseStat(pokemonInfo)




        Column(
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = "Base Stats",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))

            pokemonInfo?.stats?.forEachIndexed { i, stat ->
                PokemonStats(
                    statName = parseStatToAbbr(stat),
                    statValue = stat.base_stat,
                    statMaxValue = maxBaseStats ?: 1.0f,
                    statColor = parseStatToColor(stat),
                    animDelay = i * animDelatPerItem
                )
                Spacer(modifier = Modifier.height(4.dp))
            }



        }

}

//@Preview(showBackground = true)
//@Composable
//fun PokemonDetailScreenPreview(){
//    PokemonDetailScreen(dominantColor = Color.Red,
//        pokemonName =  "")
//}

