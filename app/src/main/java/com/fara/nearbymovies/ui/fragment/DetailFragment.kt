package com.fara.nearbymovies.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.BlurTransformation
import com.fara.nearbymovies.R
import com.fara.nearbymovies.adapter.SessionAdapter
import com.fara.nearbymovies.databinding.FragmentDetailBinding

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private lateinit var bind: FragmentDetailBinding
    private lateinit var sessionAdapter: SessionAdapter
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        bind = FragmentDetailBinding.inflate(layoutInflater)
        val detail = args.detail
        val premiere = args.premiere

        setupSessionRecyclerView()

        bind.apply {
            detail.apply {
                premiere.apply {
                    ivBackground.load(background) {
                        transformations(
                            BlurTransformation(
                                requireContext(),
                                20F,
                                2F
                            )
                        )
                    }
                    tvTitle.text = title
                    ivPoster.load(poster_url)
                    if (age.isNotEmpty()) {
                        tvAge.text = age
                        ivIconAge.visibility = View.VISIBLE
                    }
                    tvDesc.text = description
                    tvYear.text = year
                    tvCountry.text = country
                    tvGenre.text = genre
                    sessionAdapter.differ.submitList(schedule)
                    if (video_url.isNotEmpty()) btnPlayVideo.visibility = View.VISIBLE
                }
            }
        }

        onClickButtonPlayVideo(detail.video_url)

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
}