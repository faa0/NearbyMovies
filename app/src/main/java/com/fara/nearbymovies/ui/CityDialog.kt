package com.fara.nearbymovies.ui

import android.content.Context
import android.os.Bundle
import com.fara.nearbymovies.databinding.DialogCityBinding
import com.fara.nearbymovies.repository.MovieRepository
import com.fara.nearbymovies.ui.fragment.BaseDialog
import com.fara.nearbymovies.viewmodel.MovieViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CityDialog(context: Context) : BaseDialog(context) {

    private lateinit var binding: DialogCityBinding
    private lateinit var cinemaCityViewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogCityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cinemaCityViewModel = MovieViewModel(MovieRepository())

        binding.apply {
            tvCinemaCity.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    cinemaCityViewModel.setDataToLiveData()
                }
                dismiss()
            }
        }
    }
}