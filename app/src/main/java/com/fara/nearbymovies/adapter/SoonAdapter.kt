package com.fara.nearbymovies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fara.nearbymovies.databinding.ItemSoonBinding
import com.fara.nearbymovies.entity.Soon

class SoonAdapter : RecyclerView.Adapter<SoonAdapter.SoonViewHolder>() {

    inner class SoonViewHolder(val bind: ItemSoonBinding) :
        RecyclerView.ViewHolder(bind.root)

    private val differCallback = object : DiffUtil.ItemCallback<Soon>() {
        override fun areItemsTheSame(oldItem: Soon, newItem: Soon): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Soon, newItem: Soon): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SoonViewHolder(
        ItemSoonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: SoonAdapter.SoonViewHolder, position: Int) {
        val soon = differ.currentList[position]
        holder.bind.apply {
            ivSoon.load(soon.poster_url)
            tvTitle.text = soon.title
            tvDateSoon.text = soon.date

            layoutMain.setOnClickListener {
                onItemClickListener?.let { it(soon) }
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    private var onItemClickListener: ((Soon) -> Unit)? = null

    fun setOnItemClickListener(listener: (Soon) -> Unit) {
        onItemClickListener = listener
    }
}