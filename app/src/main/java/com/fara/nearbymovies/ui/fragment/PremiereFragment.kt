package com.fara.nearbymovies.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fara.nearbymovies.R
import com.fara.nearbymovies.adapter.PremiereAdapter
import com.fara.nearbymovies.adapter.SoonAdapter
import com.fara.nearbymovies.databinding.FragmentPremiereBinding
import com.fara.nearbymovies.ui.MovieActivity
import com.fara.nearbymovies.viewmodel.MovieViewModel

class PremiereFragment : Fragment(R.layout.fragment_premiere) {

    private lateinit var bind: FragmentPremiereBinding
    private lateinit var viewModel: MovieViewModel
    private lateinit var premiereAdapter: PremiereAdapter
    private lateinit var soonAdapter: SoonAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentPremiereBinding.inflate(layoutInflater)
        viewModel = (activity as MovieActivity).viewModel
        setupSoonRecyclerView()
        setupPremiereRecyclerView()

        viewModel.premiereLiveData.observe(viewLifecycleOwner, {
            premiereAdapter.differ.submitList(it)
        })
        viewModel.soonLiveData.observe(viewLifecycleOwner, {
            soonAdapter.differ.submitList(it)
        })

        return bind.root
    }

    private fun setupSoonRecyclerView() {
        soonAdapter = SoonAdapter()
        bind.soonPager.adapter = soonAdapter
    }

    private fun setupPremiereRecyclerView() {
        premiereAdapter = PremiereAdapter()
        bind.rvPremiere.adapter = premiereAdapter
    }
}