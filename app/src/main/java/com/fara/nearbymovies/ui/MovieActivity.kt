package com.fara.nearbymovies.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fara.nearbymovies.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)
    }
}