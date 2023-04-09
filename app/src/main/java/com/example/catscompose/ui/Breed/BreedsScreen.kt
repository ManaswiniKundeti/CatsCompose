package com.example.catscompose.ui.Breed

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.catscompose.model.Breed
import com.example.catscompose.ui.main.MainViewModel


@Composable
fun BreedsScreen(viewModel: MainViewModel) {
    val breedList: List<Breed> by viewModel.breedsList.collectAsState(initial = listOf())

//    LazyColumn(
//        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//    )
    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 150.dp))
    {
        items(breedList) { breed ->

            val painter = painterResource(id = com.example.catscompose.R.drawable.cat1)
            val name = breed.name
            val origin = breed.origin
            ImageCard(painter = painter, origin = origin, name = name)
//            Text(text = name)
//            Box(modifier = Modifier
//                .fillMaxWidth(0.5f)
//                .padding(16.dp)) {
//                ImageCard(painter = painter, origin = origin, name = name)
//            }
        }
    }
}

@Composable
fun ImageCard(
    painter: Painter,
    origin: String,
    name: String,
    passedModifier: Modifier = Modifier
) {
    Card(
        modifier = passedModifier.fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(15.dp),
        elevation = 5.dp

    ) {
        Box(modifier = Modifier.height(180.dp)) {
            Image(painter = painter, contentDescription = origin, contentScale = ContentScale.Crop)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = name,
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ),
                    fontSize = 20.sp)
            }
        }
    }
}