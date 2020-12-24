package com.fara.nearbymovies.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.fara.nearbymovies.R
import com.fara.nearbymovies.databinding.FragmentDetailBinding
import com.fara.nearbymovies.ui.MovieActivity
import com.fara.nearbymovies.viewmodel.MovieViewModel

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private lateinit var bind: FragmentDetailBinding
    private lateinit var viewModel: MovieViewModel
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentDetailBinding.inflate(layoutInflater)
        viewModel = (activity as MovieActivity).viewModel
        val premiere = args.premiere

        return bind.root
    }
}