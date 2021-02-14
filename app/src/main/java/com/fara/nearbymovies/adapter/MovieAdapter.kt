package com.fara.nearbymovies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fara.nearbymovies.databinding.ItemSessionBinding
import com.fara.nearbymovies.db.model.CompareMovie

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(val bind: ItemSessionBinding) :
        RecyclerView.ViewHolder(bind.root)

    val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<CompareMovie>() {
        override fun areItemsTheSame(oldItem: CompareMovie, newItem: CompareMovie): Boolean {
            return oldItem.session == newItem.session
        }

        override fun areContentsTheSame(oldItem: CompareMovie, newItem: CompareMovie): Boolean {
            return oldItem == newItem
        }
    })

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieAdapter.MovieViewHolder =
        MovieViewHolder(
            ItemSessionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: MovieAdapter.MovieViewHolder, position: Int) {
        val movie = differ.currentList[position]
        holder.bind.apply {
            tvSession.text = movie.cinema
            tvTimePrice.text = movie.session.toString()
        }
    }

    override fun getItemCount() = differ.currentList.size
}