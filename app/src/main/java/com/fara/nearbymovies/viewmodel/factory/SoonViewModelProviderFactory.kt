package com.fara.nearbymovies.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fara.nearbymovies.repository.SoonRepository
import com.fara.nearbymovies.viewmodel.SoonViewModel

@Suppress("UNCHECKED_CAST")
class SoonViewModelProviderFactory(
    private val soonRepository: SoonRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SoonViewModel(soonRepository) as T
    }
}