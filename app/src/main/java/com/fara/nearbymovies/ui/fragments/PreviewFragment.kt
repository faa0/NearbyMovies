package com.fara.nearbymovies.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fara.nearbymovies.R
import com.fara.nearbymovies.adapter.PreviewAdapter
import com.fara.nearbymovies.databinding.FragmentPreviewBinding
import com.fara.nearbymovies.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreviewFragment : Fragment(R.layout.fragment_preview) {

    private lateinit var binding: FragmentPreviewBinding
    private lateinit var previewAdapter: PreviewAdapter
    private val viewModel: MovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPreviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPreviewRecyclerView()

        binding.apply {
            viewModel.previewLD.observe(viewLifecycleOwner, {
                previewAdapter.differ.submitList(it)
            })
        }
    }

    private fun setupPreviewRecyclerView() {
        previewAdapter = PreviewAdapter()
        binding.rvPreview.adapter = previewAdapter
    }
}