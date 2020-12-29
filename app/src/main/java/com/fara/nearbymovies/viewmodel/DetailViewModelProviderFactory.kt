package com.fara.nearbymovies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fara.nearbymovies.repository.DetailRepository

@Suppress("UNCHECKED_CAST")
class DetailViewModelProviderFactory(
    private val detailRepository: DetailRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailViewModel(detailRepository) as T
    }
}