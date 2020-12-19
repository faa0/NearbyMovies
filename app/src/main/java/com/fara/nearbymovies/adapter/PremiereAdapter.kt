package com.fara.nearbymovies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fara.nearbymovies.databinding.ItemPremiereBinding
import com.fara.nearbymovies.entity.Premiere

class PremiereAdapter : RecyclerView.Adapter<PremiereAdapter.PremiereViewHolder>() {

    inner class PremiereViewHolder(val bind: ItemPremiereBinding) :
        RecyclerView.ViewHolder(bind.root)

    private val differCallback = object : DiffUtil.ItemCallback<Premiere>() {
        override fun areItemsTheSame(oldItem: Premiere, newItem: Premiere): Boolean {
            return oldItem.title == newItem.title
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
            ivPremiere.load(premiere.poster_url)
            tvTitlePremiere.text = premiere.title

            layoutMain.setOnClickListener {
                onItemClickListener?.let { it(premiere) }
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    private var onItemClickListener: ((Premiere) -> Unit)? = null

    fun setOnItemClickListener(listener: (Premiere) -> Unit) {
        onItemClickListener = listener
    }
}