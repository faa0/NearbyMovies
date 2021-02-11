package com.fara.nearbymovies.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fara.nearbymovies.repository.MovieRepository
import com.fara.nearbymovies.repository.MultiplexRepository
import com.fara.nearbymovies.viewmodel.MovieViewModel

@Suppress("UNCHECKED_CAST")
class MovieViewModelProviderFactory(
    private val movieRepository: MovieRepository,
    private val multiplexRepository: MultiplexRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieViewModel(movieRepository, multiplexRepository) as T
    }
}