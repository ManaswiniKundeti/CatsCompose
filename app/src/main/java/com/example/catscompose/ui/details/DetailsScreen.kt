package com.example.catscompose.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import timber.log.Timber

@Composable
fun BreedDetails(
    breedId: String,
    viewModel: DetailsViewModel,
    pressOnBack: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        color = MaterialTheme.colors.background
    ) {
        // run suspend functions in the scope of a composable
        LaunchedEffect(key1 = breedId, block = {
            viewModel.loadBreedById(breedId)
        })

        val breedDetail by viewModel.breedDetailFlow.collectAsState(initial = null)

        breedDetail?.let { it->
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                AsyncImage(
                    model = "https://cdn2.thecatapi.com/images/${it.referenceImageId}.jpg",
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .width(350.dp)
                        .height(350.dp),
                    onError = { Timber.e(it.toString()) }
                )
                Row(
                    modifier = Modifier.padding(5.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Name : ",
                        style = TextStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold
                        ),
                        fontSize = 20.sp)
                    Text(
                        text = it.name,
                        style = TextStyle(
                            color = Color.Blue,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily.SansSerif
                        ),
                        fontSize = 20.sp)
                }
                Row(
                    modifier = Modifier.padding(5.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Origin : ",
                        style = TextStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold
                        ),
                        fontSize = 20.sp
                    )
                    Text(
                        text = it.origin,
                        style = TextStyle(
                            color = Color.Blue,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily.SansSerif
                        ),
                        fontSize = 20.sp
                    )
                }
                Row(
                    modifier = Modifier.padding(5.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Description : ",
                        style = TextStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold
                        ),
                        fontSize = 20.sp
                    )
                    Text(
                        text = it.description,
                        style = TextStyle(
                            color = Color.Blue,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily.SansSerif
                        ),
                        fontSize = 20.sp
                    )
                }
                Row(
                    modifier = Modifier.padding(5.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Lifespan : ",
                        style = TextStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold
                        ),
                        fontSize = 20.sp)
                    it.lifeSpan?.let { it1 ->
                        Text(
                            text = it1+"yrs",
                            style = TextStyle(
                                color = Color.Blue,
                                fontWeight = FontWeight.Normal,
                                fontFamily = FontFamily.SansSerif
                            ),
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}