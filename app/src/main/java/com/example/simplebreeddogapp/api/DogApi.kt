package com.example.simplebreeddogapp.api

import com.example.simplebreeddogapp.models.BreedsResponse
import com.example.simplebreeddogapp.models.DogApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DogApi {

    @GET("breed/{breed}/images")
    suspend fun getDogImages(@Path("breed") breed: String): Response<DogApiResponse>

    @GET("breeds/list/all")
    suspend fun getAllBreeds(): Response<BreedsResponse>

}