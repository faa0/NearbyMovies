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
import com.fara.nearbymovies.entity.Soon
import com.fara.nearbymovies.ui.MovieActivity
import com.fara.nearbymovies.viewmodel.MovieViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PremiereFragment : Fragment(R.layout.fragment_premiere) {

    private lateinit var bind: FragmentPremiereBinding
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var premiereAdapter: PremiereAdapter
    private lateinit var soonAdapter: SoonAdapter
    private lateinit var soonList: List<Soon>
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
            soonLiveData.observe(viewLifecycleOwner, {
                soonAdapter.differ.submitList(it)
                soonList = it
            })
            detailLiveDataSoon.observe(viewLifecycleOwner, {
                soonAdapter.setDetailMovie(it)
            })
            premiereLiveData.observe(viewLifecycleOwner, {
                premiereAdapter.differ.submitList(it)
            })
            detailLiveData.observe(viewLifecycleOwner, {
                premiereAdapter.setDetailList(it)
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

                movieViewModel.position = positionOfSoonPager
                GlobalScope.launch(Dispatchers.IO) {
                    movieViewModel.setDataToDetailLiveData()
                }

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

        soonAdapter.setOnItemClickListener { detail, soon ->
            val bundle = Bundle().apply {
                putSerializable("detail", detail)
                putSerializable("soon", soon)
                putInt("positionOfSoonPager", positionOfSoonPager)
            }
            findNavController().navigate(
                R.id.action_premiereFragment_to_detailFragment,
                bundle
            )
        }

        premiereAdapter.setOnItemClickListener { detail, premiere ->
            val bundle = Bundle().apply {
                putSerializable("detail", detail)
                putSerializable("premiere", premiere)
            }
            findNavController().navigate(
                R.id.action_premiereFragment_to_detailFragment,
                bundle
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