package com.example.simplebreeddogapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.simplebreeddogapp.DogApplication
import com.example.simplebreeddogapp.models.BreedsResponse
import com.example.simplebreeddogapp.models.DogApiResponse
import com.example.simplebreeddogapp.repository.DogRepository
import com.example.simplebreeddogapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class DogViewModel (
    app : Application,
    private val mRepository: DogRepository
) : AndroidViewModel(app) {

    val fetchDogImages: MutableLiveData<Resource<DogApiResponse>> = MutableLiveData()

    val fetchAllBreeds: MutableLiveData<Resource<BreedsResponse>> = MutableLiveData()

    fun fetchDogImages(breed: String) =viewModelScope.launch {
        safeDogImagesNetworkCall(breed)
    }

    fun fetchAllBreeds() =viewModelScope.launch {
        safeBreedsNetworkCall()
    }

    private suspend fun safeBreedsNetworkCall() {
        fetchAllBreeds.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response = mRepository.getAllBreeds()

                Log.d("Response", response.body().toString())

                fetchAllBreeds.postValue(handleBreedsResponse(response))
            } else{
                fetchAllBreeds.postValue(Resource.Error("No internet connection"))
            }

        } catch (t: Throwable){
            when(t) {
                is IOException -> fetchAllBreeds.postValue(Resource.Error("Network Failure"))
                else -> fetchAllBreeds.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleBreedsResponse(response: Response<BreedsResponse>): Resource<BreedsResponse> {
        if(response.isSuccessful){
            response.body()?.let {resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeDogImagesNetworkCall(breed: String) {
        fetchDogImages.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response = mRepository.getDogImages(breed)
                fetchDogImages.postValue(handleBreakingNewsResponse(response))
            } else{
                fetchDogImages.postValue(Resource.Error("No internet connection"))
            }

        } catch (t: Throwable){
            when(t) {
                is IOException -> fetchDogImages.postValue(Resource.Error("Network Failure"))
                else -> fetchDogImages.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleBreakingNewsResponse(response: Response<DogApiResponse>): Resource<DogApiResponse> {
        if(response.isSuccessful){
            response.body()?.let {resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<DogApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
        else{
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}