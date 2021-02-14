package com.fara.nearbymovies.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fara.nearbymovies.repository.LocalRepository
import com.fara.nearbymovies.repository.RemoteRepository

@Suppress("UNCHECKED_CAST")
class MovieViewModelProviderFactory(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
    private val app: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieViewModel(remoteRepository, localRepository, app) as T
    }
}