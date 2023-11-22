package com.example.simplebreeddogapp.models

data class BreedsResponse(
    val message: Map<String, List<String>>,
    val status: String
)