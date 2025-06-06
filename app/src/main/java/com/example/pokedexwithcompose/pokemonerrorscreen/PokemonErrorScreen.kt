package com.example.pokedexwithcompose.pokemonerrorscreen

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.pokedexwithcompose.util.Resource
import kotlin.math.round

@Composable
fun ErrorScreen (
    error: String,
){
    val defaultDominantColor = MaterialTheme.colorScheme.surface
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(
                listOf(Color.Red, defaultDominantColor)
            ))
            .padding(bottom = 16.dp)
    )
    {
        Box(contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.9f)
                .border(
                    width = 5.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(Color.Cyan, Color.Magenta, Color.Yellow)
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .background(Color.White)
        ){
//            if (pokemonInfo is Resource.Success){
//                pokemonInfo.data?.sprites?.let(){
//                    AsyncImage(
//                        model = ImageRequest.Builder(context)
//                            .data(it.front_default)
//                            .build(),
//                        contentDescription = pokemonInfo.data.name,
//                        contentScale = ContentScale.Fit,
//                        modifier = Modifier.size(pokemonImageSize)
//                            .offset(y = topPaddind)
//                    )
//                }
//
//            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview(){
    ErrorScreen(error =  "fdsdf")
}
