package com.fara.nearbymovies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fara.nearbymovies.databinding.ItemSoonBinding
import com.fara.nearbymovies.entity.Detail
import com.fara.nearbymovies.entity.Soon

class SoonAdapter : RecyclerView.Adapter<SoonAdapter.SoonViewHolder>() {

    inner class SoonViewHolder(val bind: ItemSoonBinding) :
        RecyclerView.ViewHolder(bind.root)

    private val differCallback = object : DiffUtil.ItemCallback<Soon>() {
        override fun areItemsTheSame(oldItem: Soon, newItem: Soon): Boolean {
            return oldItem.movie_url == newItem.movie_url
        }

        override fun areContentsTheSame(oldItem: Soon, newItem: Soon): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    private var detailList = mutableListOf<Detail>()

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
                onItemClickListener?.let { it(detailList[position], soon) }
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    private var onItemClickListener: ((Detail, Soon) -> Unit)? = null

    fun setDetailList(list: MutableList<Detail>) {
        detailList = list
    }

    fun setOnItemClickListener(listener: (Detail, Soon) -> Unit) {
        onItemClickListener = listener
    }
}