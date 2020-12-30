package com.fara.nearbymovies.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fara.nearbymovies.R
import com.fara.nearbymovies.adapter.PremiereAdapter
import com.fara.nearbymovies.adapter.SoonAdapter
import com.fara.nearbymovies.databinding.FragmentPremiereBinding
import com.fara.nearbymovies.ui.MovieActivity
import com.fara.nearbymovies.viewmodel.MovieViewModel
import com.fara.nearbymovies.viewmodel.SoonViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PremiereFragment : Fragment(R.layout.fragment_premiere) {

    private lateinit var bind: FragmentPremiereBinding
    private lateinit var soonViewModel: SoonViewModel
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var premiereAdapter: PremiereAdapter
    private lateinit var soonAdapter: SoonAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        bind = FragmentPremiereBinding.inflate(layoutInflater)
        movieViewModel = (activity as MovieActivity).movieViewModel
        soonViewModel = (activity as MovieActivity).soonViewModel
        setupSoonRecyclerView()
        setupPremiereRecyclerView()

        GlobalScope.launch(Dispatchers.IO) {
            soonViewModel.setDataToLiveData()
            movieViewModel.setDataToLiveData()
            println("I'm working in thread ${Thread.currentThread().name}")
        }

        soonViewModel.soonLiveData.observe(viewLifecycleOwner, {
            soonAdapter.differ.submitList(it)
        })
        movieViewModel.premiereLiveData.observe(viewLifecycleOwner, {
            premiereAdapter.differ.submitList(it)
        })

        premiereAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("premiere", it)
            }
            findNavController().navigate(
                R.id.action_premiereFragment_to_detailFragment,
                bundle
            )
        }

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