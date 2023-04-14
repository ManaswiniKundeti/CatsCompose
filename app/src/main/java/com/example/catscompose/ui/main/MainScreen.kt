package com.example.catscompose.ui.main

import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
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
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val colors = MaterialTheme.colors
    // update system ui bar colors inside jetpack compose
    val systemUiController = rememberSystemUiController()

    var statusBarColor by remember { mutableStateOf(colors.primaryVariant) }
    var navigationBarColor by remember { mutableStateOf(colors.primaryVariant) }

    val animatedStatusBarColor by animateColorAsState(
        targetValue = statusBarColor,
        animationSpec = tween()
    )
    val animatedNavigationBarColor by animateColorAsState(
        targetValue = navigationBarColor,
        animationSpec = tween()
    )

    NavHost(navController = navController, startDestination = ScreenNavigator.Home.route) {
        // Display the UI defined inside the compose block for the home screen route
        composable(route = ScreenNavigator.Home.route) {
            MainConent(
                viewModel = hiltViewModel(),
                selectBreed = { breedId ->
                    navController.navigate(route = "${ScreenNavigator.Details.route}/$breedId")
                }
            )
            LaunchedEffect(Unit) {
                Timber.d("Navigating to Home")
            }
            LaunchedEffect(Unit) {
                statusBarColor = colors.primaryVariant
                navigationBarColor = colors.primaryVariant
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

            BreedDetails(
                breedId = selectedBreedId,
                viewModel = hiltViewModel()
            ) {
                navController.navigateUp()
            }
            LaunchedEffect(Unit) {
                statusBarColor = colors.primaryVariant
                navigationBarColor = colors.primaryVariant
            }
        }
    }

    LaunchedEffect(animatedStatusBarColor, animatedNavigationBarColor) {
        systemUiController.setStatusBarColor(animatedStatusBarColor)
        systemUiController.setNavigationBarColor(animatedNavigationBarColor)
    }
}

@Composable
fun SampleContent(name: String) {
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
fun MainConent(
    viewModel: MainViewModel,
    selectBreed: (String) -> Unit
) {
    val breedList: List<Breed> by viewModel.breedsList.collectAsState(initial = listOf())

    val isLoading: Boolean by viewModel.isLoading
    val selectedTab = BottomNavTab.getTabFromResource(viewModel.selectedTab.value)
    val tabs = BottomNavTab.values()

    Scaffold(
        backgroundColor = MaterialTheme.colors.background,
        topBar = { CatsAppBar() },
        bottomBar = { CatsBottomBar(tabs, selectedTab, viewModel) }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        Crossfade(targetState = selectedTab) {destination ->
            when (destination) {
                BottomNavTab.HOME -> HomeContent(breedList, selectBreed)
                BottomNavTab.DETAILS -> SampleContent("Sample Content")
            }

        }
    }
    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(5.dp)
                .size(10.dp, 10.dp),
            color = Color.Green
        )
    }
}

@Composable
fun CatsBottomBar(tabs: Array<BottomNavTab>, selectedTab: BottomNavTab, viewModel: MainViewModel) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.primary, //Color(0xFF651FFF),
        modifier = Modifier.navigationBarsPadding()
    ) {
        tabs.forEach { tab ->
            BottomNavigationItem(
                icon = { Icon(imageVector = tab.icon, contentDescription = null, tint = Color.White) },
                label = { Text(text = stringResource(tab.title), color = Color.White) },
                selected = tab == selectedTab,
                onClick = { viewModel.selectTab(tab.title) },
                selectedContentColor = LocalContentColor.current,
                unselectedContentColor = LocalContentColor.current,
            )
        }
    }
}

@Composable
fun CatsAppBar() {
    TopAppBar(
        elevation = 6.dp,
        backgroundColor = MaterialTheme.colors.primary,  //Color(0xFF651FFF)
        modifier = Modifier
            .statusBarsPadding()
            .height(58.dp)
//        navigationIcon = if (navController.previousBackStackEntry != null) {
//            IconButton(onClick = { navController.navigateUp() }) {
//                Icon(
//                    imageVector = Icons.Filled.ArrowBack,
//                    contentDescription = "Back"
//                )
//            }
//        } else {
//            null
//        }
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterVertically),
            text = stringResource(R.string.app_name),
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
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
            .clickable(onClick = { selectBreed(breed.id) }),
        shape = RoundedCornerShape(15.dp),
        elevation = 5.dp

    ) {
        Column(modifier = Modifier.background(MaterialTheme.colors.onBackground)) {
            Box(modifier = Modifier
                .height(180.dp)
                .background(MaterialTheme.colors.onBackground)
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
                        color = MaterialTheme.colors.primary,
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

enum class BottomNavTab(
    @StringRes val title: Int,
    val icon: ImageVector
) {
    HOME(R.string.bottom_nav_home, Icons.Filled.Home),
    DETAILS(R.string.bottom_nav_details, Icons.Filled.Details);
    companion object  {
        fun getTabFromResource(@StringRes resource: Int): BottomNavTab {
            return when (resource) {
                R.string.bottom_nav_details -> DETAILS
                else -> HOME
            }
        }
    }
}