package com.example.catscompose.network

import com.example.catscompose.model.Breed
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET

interface CatService {

    @GET("breeds")
    suspend fun fetchCatBreeds(): ApiResponse<List<Breed>>

}