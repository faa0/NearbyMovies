package com.fara.nearbymovies.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fara.nearbymovies.R
import com.fara.nearbymovies.adapter.PreviewAdapter
import com.fara.nearbymovies.adapter.SoonAdapter
import com.fara.nearbymovies.databinding.FragmentPreviewBinding
import com.fara.nearbymovies.ui.MovieActivity
import com.fara.nearbymovies.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PreviewFragment : Fragment(R.layout.fragment_preview) {

    private lateinit var binding: FragmentPreviewBinding
    private lateinit var soonAdapter: SoonAdapter
    private lateinit var previewAdapter: PreviewAdapter
    private val viewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPreviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupSoonViewPager()
        setupPreviewRecyclerView()

        binding.apply {

            viewModel.apply {
                soonLD.observe(viewLifecycleOwner, { soonAdapter.differ.submitList(it) })
                previewLD.observe(viewLifecycleOwner, { previewAdapter.differ.submitList(it) })
                errorLD.observe(viewLifecycleOwner, {
                    if (it.isNotEmpty()) {
                        tvError.text = it
                        tvError.visibility = View.VISIBLE
                    }
                })
            }

            previewAdapter.setOnItemClickListener { position, preview ->

                viewModel.positionPreview = position
                lifecycleScope.launch(Dispatchers.IO) { viewModel.updateDetailPreview() }

                val bundle = Bundle().apply {
                    putSerializable("preview", preview)
                }
                findNavController().navigate(
                    R.id.action_premiereFragment_to_detailFragment,
                    bundle
                )
            }
        }

        onBackPressed()
    }

    private fun setupPreviewRecyclerView() {
        previewAdapter = PreviewAdapter()
        binding.rvPreview.adapter = previewAdapter
    }

    private fun setupSoonViewPager() {
        soonAdapter = SoonAdapter()
        binding.soonPager.adapter = soonAdapter
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