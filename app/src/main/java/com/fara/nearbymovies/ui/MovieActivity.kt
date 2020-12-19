package com.fara.nearbymovies.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fara.nearbymovies.R
import com.fara.nearbymovies.repository.MovieRepository
import com.fara.nearbymovies.viewmodel.MovieViewModel
import com.fara.nearbymovies.viewmodel.MovieViewModelProviderFactory

class MovieActivity : AppCompatActivity() {

    lateinit var viewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val movieRepository = MovieRepository()
        val viewModelProviderFactory = MovieViewModelProviderFactory(movieRepository)
        viewModel = ViewModelProvider(
            this,
            viewModelProviderFactory
        )[MovieViewModel::class.java]
    }
}