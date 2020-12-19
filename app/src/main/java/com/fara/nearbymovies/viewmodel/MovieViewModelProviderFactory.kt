package com.fara.nearbymovies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fara.nearbymovies.repository.MovieRepository

@Suppress("UNCHECKED_CAST")
class MovieViewModelProviderFactory(private val movieRepository: MovieRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieViewModel(movieRepository) as T
    }
}