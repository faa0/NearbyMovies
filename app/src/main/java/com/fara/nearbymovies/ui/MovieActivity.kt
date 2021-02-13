package com.fara.nearbymovies.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fara.nearbymovies.R
import com.fara.nearbymovies.repository.MayakRepositiory
import com.fara.nearbymovies.repository.MovieRepository
import com.fara.nearbymovies.repository.MultiplexRepository
import com.fara.nearbymovies.viewmodel.MovieViewModel
import com.fara.nearbymovies.viewmodel.factory.MovieViewModelProviderFactory

class MovieActivity : AppCompatActivity() {

    lateinit var movieViewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val movieRepository = MovieRepository()
        val multiplexRepository = MultiplexRepository()
        val mayakRepositiory = MayakRepositiory()

        val movieViewModelProviderFactory =
            MovieViewModelProviderFactory(movieRepository, multiplexRepository, mayakRepositiory)
        movieViewModel = ViewModelProvider(
            this,
            movieViewModelProviderFactory
        )[MovieViewModel::class.java]
    }
}