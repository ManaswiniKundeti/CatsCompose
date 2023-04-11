package com.example.catscompose.ui.Breed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.catscompose.BuildConfig
import com.example.catscompose.model.Breed
import com.example.catscompose.ui.main.MainViewModel
import timber.log.Timber



@Composable
fun TitleText(name: String) {
    Text(text = "Welcome, $name!",
        style = TextStyle (
            color = Color.Black,
            fontWeight = FontWeight.Bold
        ),
        fontSize = 30.sp
    )
    Timber.d(BuildConfig.CatsApiKey)
}

@Composable
fun BreedsList(viewModel: MainViewModel) {
    val breedList: List<Breed> by viewModel.breedsList.collectAsState(initial = listOf())

    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 150.dp))
    {
        items(breedList) { breed ->
            breed.referenceImageId?.let {
                ImageCard(imageRes = it, origin = breed.origin, name = breed.name) }
        }
    }
}

@Composable
fun ImageCard(
    imageRes: String,
    origin: String,
    name: String,
    passedModifier: Modifier = Modifier
) {
    Card(
        modifier = passedModifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(15.dp),
        elevation = 5.dp

    ) {
        Column {
            Box(modifier = Modifier
                .height(180.dp)
                .background(Color.LightGray)
                .padding(10.dp)
            ) {
                AsyncImage(
                    model = "https://cdn2.thecatapi.com/images/${imageRes}.jpg",
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .clip(CircleShape)
                        .width(150.dp)
                        .height(150.dp),
                    onError = { Timber.e(it.toString()) }
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = name,
                    style = TextStyle(
                        color = Color.Blue,
                        fontWeight = FontWeight.Bold
                    ),
                    fontSize = 20.sp)
            }
        }
    }
}