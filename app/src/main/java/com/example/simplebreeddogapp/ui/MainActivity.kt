package com.example.simplebreeddogapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.simplebreeddogapp.databinding.ActivityMainBinding
import com.example.simplebreeddogapp.repository.DogRepository

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: DogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val dogRepository = DogRepository()
        val viewModelProviderFactory = DogViewModelFactory(application, dogRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[DogViewModel::class.java]
        val view = binding.root
        setContentView(view)
    }
}