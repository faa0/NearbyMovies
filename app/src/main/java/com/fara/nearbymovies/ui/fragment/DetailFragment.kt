package com.fara.nearbymovies.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.fara.nearbymovies.R
import com.fara.nearbymovies.adapter.SessionAdapter
import com.fara.nearbymovies.databinding.FragmentDetailBinding
import com.fara.nearbymovies.ui.MovieActivity
import com.fara.nearbymovies.viewmodel.MovieViewModel
import jp.wasabeef.glide.transformations.BlurTransformation

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private lateinit var bind: FragmentDetailBinding
    private lateinit var sessionAdapter: SessionAdapter
    private lateinit var movieViewModel: MovieViewModel
    private val args: DetailFragmentArgs by navArgs()
    private var positionOfSoonPager = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        bind = FragmentDetailBinding.inflate(layoutInflater)
        movieViewModel = (activity as MovieActivity).movieViewModel
        val premiere = args.premiere
        val soon = args.soon
        val positionOfPremiereAdapter = args.positionOfPremiereAdapter
        positionOfSoonPager = args.positionOfSoonPager

        setupSessionRecyclerView()

        if (positionOfPremiereAdapter == -1) {
            movieViewModel.detailLiveDataSoon.observe(viewLifecycleOwner, {
                bind.apply {
                    it.apply {
                        Glide
                            .with(ivBackground.context)
                            .load(background)
                            .apply(bitmapTransform(BlurTransformation(20, 1)))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(ivBackground)
                        tvDesc.text = description
                        tvYear.text = year
                        tvCountry.text = country
                        tvGenre.text = genre
                        sessionAdapter.differ.submitList(schedule)
                        if (video_url.isNotEmpty()) btnPlayVideo.visibility = View.VISIBLE
                        onClickButtonPlayVideo(video_url)
                    }
                }
            })
        } else {
            movieViewModel.detailLiveDataPremiere.observe(viewLifecycleOwner, {
                bind.apply {
                    it.apply {
                        Glide
                            .with(ivBackground.context)
                            .load(background)
                            .apply(bitmapTransform(BlurTransformation(20, 1)))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(ivBackground)
                        tvDesc.text = description
                        tvYear.text = year
                        tvCountry.text = country
                        tvGenre.text = genre
                        sessionAdapter.differ.submitList(schedule)
                        if (video_url.isNotEmpty()) btnPlayVideo.visibility = View.VISIBLE
                        onClickButtonPlayVideo(video_url)
                    }
                }
            })
        }

        bind.apply {
            when (soon?.title) {
                null -> tvTitle.text = premiere?.title
                else -> tvTitle.text = soon.title
            }
            when (soon?.poster_url) {
                null -> Glide
                    .with(ivPoster.context)
                    .load(premiere?.poster_url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivPoster)
                else -> Glide
                    .with(ivPoster.context)
                    .load(soon.poster_url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivPoster)
            }
            when {
                premiere?.age?.isNotEmpty() == true -> {
                    tvAge.text = premiere.age
                    ivIconAge.visibility = View.VISIBLE
                }
            }
        }

        onBackPressed()

        return bind.root
    }

    private fun setupSessionRecyclerView() {
        sessionAdapter = SessionAdapter()
        bind.rvSession.adapter = sessionAdapter
    }

    private fun onClickButtonPlayVideo(videoUrl: String) {
        bind.btnPlayVideo.setOnClickListener {
            val url: Uri = Uri.parse(videoUrl)
            startActivity(Intent(Intent.ACTION_VIEW, url))
        }
    }

    private fun onBackPressed() {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val bundle = Bundle().apply {
                        putInt("positionOfSoonPager", positionOfSoonPager)
                        putBoolean("state", true)
                    }
                    findNavController().navigate(
                        R.id.action_detailFragment_to_premiereFragment,
                        bundle
                    )
                }
            }
            )
    }
}