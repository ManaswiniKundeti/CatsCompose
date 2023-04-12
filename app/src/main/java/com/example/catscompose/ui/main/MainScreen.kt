package com.example.catscompose.ui.main

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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.catscompose.BuildConfig
import com.example.catscompose.model.Breed
import com.example.catscompose.ui.details.BreedDetails
import com.example.catscompose.ui.main.MainViewModel
import timber.log.Timber

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ScreenNavigator.Home.route) {
        // Display the UI defined inside the compose block for the home screen route
        composable(route = ScreenNavigator.Home.route) {
            Column(modifier = Modifier.padding(10.dp)) {
                TitleText(name = "cat lover")
                Spacer(modifier = Modifier.height(20.dp))
                BreedsList(
                    viewModel = hiltViewModel(),
                    selectBreed = { breedId ->
                        navController.navigate(route = "${ScreenNavigator.Details.route}/$breedId")
                    }
                )
                LaunchedEffect(Unit) {
                    Timber.d("Navigating to Home")
                }
            }
        }

        composable(
            route = ScreenNavigator.Details.routeWithArgument,
            arguments = listOf(
                navArgument(ScreenNavigator.Details.argument0) {
                    type = NavType.StringType
                }
            )
        ) {
            val selectedBreedId = it.arguments?.getString(ScreenNavigator.Details.argument0) ?: return@composable

            BreedDetails(selectedBreedId) {
                navController.navigateUp()
            }

        }

    }
}

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
fun BreedsList(viewModel: MainViewModel, selectBreed: (Long) -> Unit) {

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

sealed class ScreenNavigator(val route: String) {
    object Home: ScreenNavigator("Home")
    object Details: ScreenNavigator("Details") {
        const val routeWithArgument: String = "Details/{breedId}"
        const val argument0: String = "breedId"
    }
}

