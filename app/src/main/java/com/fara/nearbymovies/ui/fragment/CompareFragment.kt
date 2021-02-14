package com.fara.nearbymovies.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.fara.nearbymovies.R
import com.fara.nearbymovies.adapter.SessionAdapter
import com.fara.nearbymovies.databinding.FragmentCompareBinding
import com.fara.nearbymovies.entity.Session
import com.fara.nearbymovies.ui.MovieActivity
import com.fara.nearbymovies.viewmodel.MovieViewModel

class CompareFragment : Fragment(R.layout.fragment_compare) {

    private lateinit var bind: FragmentCompareBinding
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var sessionAdapter: SessionAdapter
    private val args: CompareFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentCompareBinding.inflate(layoutInflater)
        movieViewModel = (activity as MovieActivity).movieViewModel
        val title = args.title

        setupPremiereRecyclerView()

        bind.apply {

            movieViewModel.getSessionByTitle(title).observe(viewLifecycleOwner, {
                tvTitle.text = title
                val movieList = mutableListOf<Session>()
                for (i in it) {
                    movieList += Session(i.cinema, i.session[0].time_price)
                }
                sessionAdapter.differ.submitList(movieList)
            })
        }
        return bind.root
    }

    private fun setupPremiereRecyclerView() {
        sessionAdapter = SessionAdapter()
        bind.rvSession.adapter = sessionAdapter
    }
}
