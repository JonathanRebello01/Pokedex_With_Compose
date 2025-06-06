package com.example.pokedexwithcompose.pokemonerrorscreen

import android.graphics.Bitmap
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.palette.graphics.Palette
import coil3.request.SuccessResult
import coil3.toBitmap

class PokemonErrorViewModel : ViewModel() {


    fun calcDominentColor(drawable: SuccessResult, onFinish: (Color) -> Unit) {
        val bmp = drawable.image.toBitmap().copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}