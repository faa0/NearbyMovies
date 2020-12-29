package com.fara.nearbymovies.ui.fragment

import android.os.Bundle
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
            getDataFromNetwork()
            setDataToView(it)
        })

        return bind.root
    }

    private fun getDataFromNetwork() {
        bind.apply {
            detailViewModel.apply {
                GlobalScope.launch {
                    description = getDescription()
                    videoUrl = getVideoUrl()
                    year = getYear()
                    country = getCountry()
                    genre = getGenre()
                    background = getBackground()
                }
            }
        }
    }

    private fun setDataToView(it: Premiere) {
        GlobalScope.launch(Dispatchers.Main) {
            delay(2000)
            bind.apply {
                tvDesc.text = description
                tvYear.text = year
                tvCountry.text = country
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
                    it.age.isNotEmpty() -> {
                        tvAge.text = it.age
                        tvAge.visibility = View.VISIBLE
                        ivIconAge.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}