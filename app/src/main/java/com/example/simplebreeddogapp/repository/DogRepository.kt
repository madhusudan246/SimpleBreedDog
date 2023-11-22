package com.example.simplebreeddogapp.repository

import com.example.simplebreeddogapp.api.RetrofitInstance

class DogRepository {

    suspend fun getDogImages(breed: String) = RetrofitInstance.api.getDogImages(breed)

    suspend fun getAllBreeds() = RetrofitInstance.api.getAllBreeds()

}