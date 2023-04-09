package com.example.catscompose.ui.Breed

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.catscompose.model.Breed
import com.example.catscompose.ui.main.MainViewModel


@Composable
fun BreedsScreen(viewModel: MainViewModel) {
    val breedList: List<Breed> by viewModel.breedsList.collectAsState(initial = listOf())

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(breedList) { breed ->
            val name = breed.name
            val origin = breed.origin
            Text(text = name)
        }
    }
}