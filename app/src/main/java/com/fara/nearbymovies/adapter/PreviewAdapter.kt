package com.fara.nearbymovies.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.fara.nearbymovies.databinding.ItemPreviewBinding
import com.fara.nearbymovies.db.model.Preview

class PreviewAdapter : RecyclerView.Adapter<PreviewAdapter.PreviewViewHolder>() {

    inner class PreviewViewHolder(val binding: ItemPreviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Preview>() {
        override fun areItemsTheSame(oldItem: Preview, newItem: Preview) = oldItem.id == newItem.id

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Preview, newItem: Preview) = oldItem == newItem
    })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PreviewViewHolder(
        ItemPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: PreviewAdapter.PreviewViewHolder, position: Int) {
        bind(holder, position)
    }

    override fun getItemCount() = differ.currentList.size

    private fun bind(holder: PreviewViewHolder, position: Int) {
        val preview = differ.currentList[position]
        holder.binding.apply {
            Glide.with(ivPreview.context)
                .load(preview.poster_url)
                .transform(CenterCrop(), RoundedCorners(10))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivPreview)
            tvTitlePreview.text = preview.title

            root.setOnClickListener { onItemClickListener?.let { it(position, preview) } }
        }
    }

    private var onItemClickListener: ((Int, Preview) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int, Preview) -> Unit) {
        onItemClickListener = listener
    }
}