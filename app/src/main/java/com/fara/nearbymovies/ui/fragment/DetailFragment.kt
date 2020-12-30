package com.fara.nearbymovies.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.BlurTransformation
import com.fara.nearbymovies.R
import com.fara.nearbymovies.databinding.FragmentDetailBinding
import com.fara.nearbymovies.entity.Premiere
import com.fara.nearbymovies.ui.MovieActivity
import com.fara.nearbymovies.viewmodel.DetailViewModel
import kotlinx.coroutines.*

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private lateinit var bind: FragmentDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private val args: DetailFragmentArgs by navArgs()
    private var background = ""
    private var description = ""
    private var videoUrl = ""
    private var year = ""
    private var country = ""
    private var genre = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        bind = FragmentDetailBinding.inflate(layoutInflater)
        detailViewModel = (activity as MovieActivity).detailViewModel
        val premiere = args.premiere

        detailViewModel.premiereLiveData.value = premiere

        detailViewModel.premiereLiveData.observe(viewLifecycleOwner, {
            GlobalScope.launch(Dispatchers.IO) {
                getDataFromNetwork()
                println("I'm working in thread ${Thread.currentThread().name}")

                withContext(Dispatchers.Main) {
                    setDataToViews(it)
                    println("I'm working in thread ${Thread.currentThread().name}")
                }
            }
        })

        onClickButtonPlayVideo()

        return bind.root
    }

    private fun onClickButtonPlayVideo() {
        bind.btnPlayVideo.setOnClickListener {
            val url: Uri = Uri.parse(videoUrl)
            startActivity(Intent(Intent.ACTION_VIEW, url))
        }
    }

    private fun getDataFromNetwork() {
        bind.apply {
            detailViewModel.apply {
                description = getDescription()
                year = getYear()
                country = getCountry()
                genre = getGenre()
                background = getBackground()
                videoUrl = getVideoUrl()
                Log.d("tea", detailViewModel.getListOfSessions().toString())
                Log.d("tea", detailViewModel.getListOfTimes().toString())
            }
        }
    }

    private fun setDataToViews(it: Premiere) {
        bind.apply {
            tvDesc.text = description
            tvGenre.text = genre
            ivBackground.load(background) {
                transformations(
                    BlurTransformation(
                        requireContext(),
                        20F,
                        2F
                    )
                )
            }
            tvTitle.text = it.title
            ivPoster.load(it.poster_url)
            when {
                year.isNotEmpty() -> {
                    tvYear.text = year
                    ivIconYear.visibility = View.VISIBLE
                }
            }
            when {
                country.isNotEmpty() -> {
                    tvCountry.text = country
                    ivIconCountry.visibility = View.VISIBLE
                }
            }
            when {
                it.age.isNotEmpty() -> {
                    tvAge.text = it.age
                    tvAge.visibility = View.VISIBLE
                    ivIconAge.visibility = View.VISIBLE
                }
            }
            when {
                videoUrl.isNotEmpty() -> {
                    btnPlayVideo.visibility = View.VISIBLE
                }
            }
            progressBar.visibility = View.GONE
        }
    }
}