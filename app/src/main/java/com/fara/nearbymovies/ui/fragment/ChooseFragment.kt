package com.fara.nearbymovies.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fara.nearbymovies.R
import com.fara.nearbymovies.adapter.PremiereAdapter
import com.fara.nearbymovies.databinding.FragmentChooseBinding
import com.fara.nearbymovies.ui.MovieActivity
import com.fara.nearbymovies.viewmodel.MovieViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ChooseFragment : Fragment(R.layout.fragment_choose) {

    private lateinit var bind: FragmentChooseBinding
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var premiereAdapter: PremiereAdapter
    private var cinemaTitle = "CinemaCity"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        bind = FragmentChooseBinding.inflate(layoutInflater)

        movieViewModel = (activity as MovieActivity).movieViewModel

        premiereAdapter = PremiereAdapter()

        bind.apply {
            tvCinemaCity.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    movieViewModel.setCinemaCityLiveData()
                    movieViewModel.setCinemaCityListDetailToDb()
                }
                movieViewModel.premiereLiveData.observe(
                    viewLifecycleOwner,
                    { premiereAdapter.differ.submitList(it) })
                movieViewModel.cinema = 0
                cinemaTitle = "CinemaCity"
            }

            tvMultiplex.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    movieViewModel.setMultiplexLiveData()
                    movieViewModel.setMultiplexListDetailToDb()
                }
                movieViewModel.premiereLiveData.observe(viewLifecycleOwner,
                    { premiereAdapter.differ.submitList(it) })
                movieViewModel.cinema = 1
                cinemaTitle = "Multiplex"
            }

            tvMayakovskiy.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    movieViewModel.setMayakLiveData()
                    movieViewModel.setMayakListDetailToDb()
                }
                movieViewModel.premiereLiveData.observe(
                    viewLifecycleOwner,
                    { premiereAdapter.differ.submitList(it) })
                movieViewModel.cinema = 2
                cinemaTitle = "Mayakovskogo"
            }
        }

        onBackPressed()

        return bind.root
    }

    private fun onBackPressed() {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val bundle = Bundle().apply {
                        putString("cinemaTitle", cinemaTitle)
                    }
                    findNavController().navigate(
                        R.id.action_chooseFragment_to_premiereFragment,
                        bundle
                    )
                }
            }
            )
    }
}