package com.example.simplebreeddogapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.simplebreeddogapp.repository.DogRepository

class DogViewModelFactory (
    private val app: Application,
    private val mRepository: DogRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DogViewModel(app, mRepository) as T
    }

}