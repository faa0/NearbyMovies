package com.fara.nearbymovies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fara.nearbymovies.databinding.ItemSessionBinding
import com.fara.nearbymovies.entity.Session

class SessionAdapter : RecyclerView.Adapter<SessionAdapter.SessionViewHolder>() {

    inner class SessionViewHolder(val bind: ItemSessionBinding) :
        RecyclerView.ViewHolder(bind.root)

    private val differCallback = object : DiffUtil.ItemCallback<Session>() {
        override fun areItemsTheSame(oldItem: Session, newItem: Session): Boolean {
            return oldItem.time_price == newItem.time_price
        }

        override fun areContentsTheSame(oldItem: Session, newItem: Session): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SessionViewHolder(
        ItemSessionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: SessionAdapter.SessionViewHolder, position: Int) {
        val session = differ.currentList[position]
        holder.bind.apply {
            tvSession.text = session.session
            tvTimePrice.text = session.time_price
        }
    }

    override fun getItemCount() = differ.currentList.size
}