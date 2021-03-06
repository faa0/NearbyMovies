package com.fara.nearbymovies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.fara.nearbymovies.databinding.ItemSoonBinding
import com.fara.nearbymovies.db.model.Preview

class SoonAdapter : RecyclerView.Adapter<SoonAdapter.SoonViewHolder>() {

    inner class SoonViewHolder(val binding: ItemSoonBinding) : RecyclerView.ViewHolder(binding.root)

    val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Preview>() {
        override fun areItemsTheSame(oldItem: Preview, newItem: Preview) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Preview, newItem: Preview) = oldItem == newItem
    })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SoonViewHolder(
        ItemSoonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: SoonAdapter.SoonViewHolder, position: Int) {
        bind(holder, position)
    }

    override fun getItemCount() = differ.currentList.size

    private fun bind(holder: SoonAdapter.SoonViewHolder, position: Int) {
        val soon = differ.currentList[position]
        holder.binding.apply {
            Glide.with(ivSoon.context)
                .load(soon.poster_url)
                .transform(CenterCrop(), RoundedCorners(10))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivSoon)
            tvTitle.text = soon.title
            tvDateSoon.text = soon.date
        }
    }
}