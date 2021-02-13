package com.fara.nearbymovies.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fara.nearbymovies.R
import com.fara.nearbymovies.db.AppDatabase
import com.fara.nearbymovies.repository.LocalRepository
import com.fara.nearbymovies.repository.RemoteRepository
import com.fara.nearbymovies.viewmodel.MovieViewModel
import com.fara.nearbymovies.viewmodel.factory.MovieViewModelProviderFactory

class MovieActivity : AppCompatActivity() {

    lateinit var movieViewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val remoteRepository = RemoteRepository()
        val localRepository = LocalRepository(AppDatabase(this))

        val movieViewModelProviderFactory =
            MovieViewModelProviderFactory(remoteRepository, localRepository, application)
        movieViewModel = ViewModelProvider(
            this,
            movieViewModelProviderFactory
        )[MovieViewModel::class.java]
    }
}