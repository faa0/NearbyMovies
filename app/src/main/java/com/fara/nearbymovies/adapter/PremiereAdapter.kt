package com.fara.nearbymovies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fara.nearbymovies.databinding.ItemPremiereBinding
import com.fara.nearbymovies.entity.Premiere

class PremiereAdapter : RecyclerView.Adapter<PremiereAdapter.PremiereViewHolder>() {

    inner class PremiereViewHolder(val bind: ItemPremiereBinding) :
        RecyclerView.ViewHolder(bind.root)

    private val differCallback = object : DiffUtil.ItemCallback<Premiere>() {
        override fun areItemsTheSame(oldItem: Premiere, newItem: Premiere): Boolean {
            return oldItem.movie_url == newItem.movie_url
        }

        override fun areContentsTheSame(oldItem: Premiere, newItem: Premiere): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PremiereViewHolder(
        ItemPremiereBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: PremiereAdapter.PremiereViewHolder, position: Int) {
        val premiere = differ.currentList[position]
        holder.bind.apply {
            Glide
                .with(ivPremiere.context)
                .load(premiere.poster_url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivPremiere)
            tvTitlePremiere.text = premiere.title

            layoutMain.setOnClickListener {
                onItemClickListener?.let { it(position, premiere) }
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    private var onItemClickListener: ((Int, Premiere) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int, Premiere) -> Unit) {
        onItemClickListener = listener
    }
}