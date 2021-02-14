package com.fara.nearbymovies.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.fara.nearbymovies.R
import com.fara.nearbymovies.adapter.PremiereAdapter
import com.fara.nearbymovies.adapter.SoonAdapter
import com.fara.nearbymovies.databinding.FragmentPremiereBinding
import com.fara.nearbymovies.ui.MovieActivity
import com.fara.nearbymovies.viewmodel.MovieViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PremiereFragment : Fragment(R.layout.fragment_premiere) {

    private lateinit var bind: FragmentPremiereBinding
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var premiereAdapter: PremiereAdapter
    private lateinit var soonAdapter: SoonAdapter
    private var positionOfSoonPager = 0
    private var state = false
    private val args: PremiereFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        bind = FragmentPremiereBinding.inflate(layoutInflater)
        movieViewModel = (activity as MovieActivity).movieViewModel
        setupSoonRecyclerView()
        setupPremiereRecyclerView()

        state = args.state

        movieViewModel.apply {
            soonLiveData.observe(viewLifecycleOwner, { soonAdapter.differ.submitList(it) })
            premiereLiveData.observe(viewLifecycleOwner, {
                premiereAdapter.differ.submitList(it)
                insertCinemaList(it)
                GlobalScope.launch {
                    setCinemaCityListDetailToDb()
                }
            })
        }

        bind.soonPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when {
                    args.positionOfSoonPager >= 0 && !state -> positionOfSoonPager = position
                    args.positionOfSoonPager == 0 && state -> {
                        positionOfSoonPager = position
                        state = false
                    }
                    else -> positionOfSoonPager = args.positionOfSoonPager
                }
                movieViewModel.positionSoon = positionOfSoonPager
                GlobalScope.launch(Dispatchers.IO) { movieViewModel.updateDetailSoon() }

                if (state) bind.soonPager.setCurrentItem(positionOfSoonPager, false)
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (bind.soonPager.currentItem > 0 && positionOffsetPixels > 50) {
                    state = false
                }
            }
        })

        soonAdapter.setOnItemClickListener { soon ->
            val bundle = Bundle().apply {
                putSerializable("soon", soon)
                putInt("positionOfSoonPager", positionOfSoonPager)
            }
            findNavController().navigate(
                R.id.action_premiereFragment_to_detailFragment,
                bundle
            )
        }

        premiereAdapter.setOnScheduleClickListener { city, title ->
            val bundle = Bundle().apply {
                putSerializable("city", city)
                putSerializable("title", title)
            }
            findNavController().navigate(
                R.id.action_premiereFragment_to_compareFragment,
                bundle
            )
        }

        premiereAdapter.setOnItemClickListener { position, cinema ->
            movieViewModel.positionPremiere = position
            GlobalScope.launch(Dispatchers.IO) { movieViewModel.updateDetailPremiere() }

            GlobalScope.launch(Dispatchers.Main) {
                delay(200)
                val bundle = Bundle().apply {
                    putSerializable("positionOfPremiereAdapter", position)
                    putSerializable("cinema", cinema)
                    putSerializable("positionOfSoonPager", positionOfSoonPager)
                }
                findNavController().navigate(
                    R.id.action_premiereFragment_to_detailFragment,
                    bundle
                )
            }
        }

        bind.tvCity.setOnClickListener {
            findNavController().navigate(
                R.id.action_premiereFragment_to_chooseFragment
            )
        }

        onBackPressed()

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

    private fun onBackPressed() {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    (activity as MovieActivity).finish()
                }
            }
            )
    }
}