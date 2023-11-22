package com.example.simplebreeddogapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.simplebreeddogapp.adapter.PhotoRVAdapter
import com.example.simplebreeddogapp.databinding.FragmentHomeBinding
import com.example.simplebreeddogapp.ui.DogViewModel
import com.example.simplebreeddogapp.ui.MainActivity
import com.example.simplebreeddogapp.utils.Resource

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding?=null
    private val binding get() = _binding
    private lateinit var viewModel: DogViewModel
    private lateinit var imagesRVAdapter: PhotoRVAdapter
    private var breed: String = ""
    private var breedList = mutableListOf<String>()
    private var photoList: List<String> = listOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchAllBreeds()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding?.root

        viewModel = (activity as MainActivity).viewModel
        setUpRecyclerView()
        setUpListeners()

        viewModel.fetchDogImages.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { dogResponse ->
                        photoList = dogResponse.message
                        imagesRVAdapter.updateList(photoList)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e("FETCH DOG IMAGES", "An error occurred: $message")
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }

        viewModel.fetchAllBreeds.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { breedResponse ->
                        breedResponse.message.forEach { (breed, subBreeds) ->
                            if (subBreeds.isNotEmpty()) {
                                subBreeds.forEach { subBreed ->
                                    breedList.add("$subBreed $breed")
                                }
                            } else {
                                breedList.add(breed)
                            }
                        }

                        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, breedList.toList())
                        binding?.filledExposedDropdown?.setAdapter(adapter)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e("FETCH DOG IMAGES", "An error occurred: $message")
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }

        binding?.filledExposedDropdown?.setOnItemClickListener { parent, _, position, _ ->
            breed = parent.getItemAtPosition(position).toString()
        }

        return view
    }

    private fun setUpListeners() {
        binding?.buttonFetchImages?.setOnClickListener {
            if (breed.isNotEmpty()) viewModel.fetchDogImages(breed.substringAfterLast(" "))
        }
    }

    private fun hideProgressBar() {
        binding?.homeLoader?.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding?.homeLoader?.visibility = View.VISIBLE
    }

    private fun setUpRecyclerView() {
        imagesRVAdapter = PhotoRVAdapter(photoList, requireContext())
        binding?.recyclerViewImages?.apply {
            adapter = imagesRVAdapter
            val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            layoutManager = staggeredGridLayoutManager
        }
    }
}