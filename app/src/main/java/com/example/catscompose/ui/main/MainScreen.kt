package com.example.catscompose.ui.main

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import timber.log.Timber
import com.example.catscompose.R

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ScreenNavigator.Home.route) {
        // Display the UI defined inside the compose block for the home screen route
        composable(route = ScreenNavigator.Home.route) {
            Column(modifier = Modifier.padding(10.dp)) {
                TitleText(name = "cat lover")
                Spacer(modifier = Modifier.height(20.dp))
                MainConent(
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
fun MainConent(viewModel: MainViewModel, selectBreed: (String) -> Unit) {

    val breedList: List<Breed> by viewModel.breedsList.collectAsState(initial = listOf())

    HomeContent(breedList, selectBreed)
}

@Composable
fun HomeContent(breedList: List<Breed>, selectBreed: (String) -> Unit) {
    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 150.dp))
    {
        items(breedList) { breed ->
            ImageCard(breed, selectBreed)
        }
    }
}

@Composable
fun ImageCard(
    breed: Breed,
    selectBreed: (String) -> Unit,
    passedModifier: Modifier = Modifier
) {
    Card(
        modifier = passedModifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = {selectBreed(breed.id)}),
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
                    model = "https://cdn2.thecatapi.com/images/${breed.referenceImageId}.jpg",
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
                    text = breed.name,
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
