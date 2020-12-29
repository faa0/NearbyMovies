package com.fara.nearbymovies.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fara.nearbymovies.R
import com.fara.nearbymovies.repository.DetailRepository
import com.fara.nearbymovies.repository.MovieRepository
import com.fara.nearbymovies.repository.SoonRepository
import com.fara.nearbymovies.viewmodel.DetailViewModel
import com.fara.nearbymovies.viewmodel.MovieViewModel
import com.fara.nearbymovies.viewmodel.SoonViewModel
import com.fara.nearbymovies.viewmodel.factory.DetailViewModelProviderFactory
import com.fara.nearbymovies.viewmodel.factory.MovieViewModelProviderFactory
import com.fara.nearbymovies.viewmodel.factory.SoonViewModelProviderFactory

class MovieActivity : AppCompatActivity() {

    lateinit var soonViewModel: SoonViewModel
    lateinit var movieViewModel: MovieViewModel
    lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val soonRepository = SoonRepository()
        val movieRepository = MovieRepository()
        val detailRepository = DetailRepository()

        val soonViewModelProviderFactory = SoonViewModelProviderFactory(soonRepository)
        soonViewModel = ViewModelProvider(
            this,
            soonViewModelProviderFactory
        )[SoonViewModel::class.java]

        val movieViewModelProviderFactory = MovieViewModelProviderFactory(movieRepository)
        movieViewModel = ViewModelProvider(
            this,
            movieViewModelProviderFactory
        )[MovieViewModel::class.java]

        val detailViewModelProviderFactory = DetailViewModelProviderFactory(detailRepository)
        detailViewModel = ViewModelProvider(
            this,
            detailViewModelProviderFactory
        )[DetailViewModel::class.java]
    }
}