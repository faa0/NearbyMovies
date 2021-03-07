package com.fara.nearbymovies.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.fara.nearbymovies.R
import com.fara.nearbymovies.databinding.FragmentDetailBinding
import com.fara.nearbymovies.db.model.Detail
import com.fara.nearbymovies.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: MovieViewModel by activityViewModels()
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preview = args.preview

        binding.apply {

            Glide.with(ivPoster.context)
                .load(preview.poster_url)
                .transform(CenterCrop(), RoundedCorners(10))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivPoster)
            tvTitle.text = preview.title
            preview.age?.let {
                ivIconAge.visibility = View.VISIBLE
                tvAge.text = preview.age
            }

            viewModel.detailPreviewLD.observe(viewLifecycleOwner, {
                it.apply {
                    Glide.with(ivBackground.context)
                        .load(background)
                        .transform(CenterCrop(), RoundedCorners(10))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivBackground)
                    tvDesc.text = description
                    tvYear.text = year
                    tvCountry.text = country
                    tvGenre.text = genre
                    video_url?.let { video_url ->
                        btnPlayVideo.visibility = View.VISIBLE
                        onClickButtonPlayVideo(video_url)
                    }
                }
            })
        }

        onBackPressed()
    }

    private fun onClickButtonPlayVideo(videoUrl: String) {
        binding.btnPlayVideo.setOnClickListener {
            val url: Uri = Uri.parse(videoUrl)
            startActivity(Intent(Intent.ACTION_VIEW, url))
        }
    }

    private fun onBackPressed() {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    findNavController().navigate(R.id.action_detailFragment_to_premiereFragment)

                    viewModel.detailPreviewLD.postValue(
                        Detail(
                            preview_id = -1, description = "", year = "", country = "", genre = ""
                        )
                    )
                }
            }
            )
    }
}

