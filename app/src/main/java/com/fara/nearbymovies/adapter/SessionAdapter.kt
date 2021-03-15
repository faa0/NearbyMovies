package com.fara.nearbymovies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fara.nearbymovies.databinding.ItemSessionBinding
import com.fara.nearbymovies.db.model.Session

class SessionAdapter : RecyclerView.Adapter<SessionAdapter.SessionViewHolder>() {

    inner class SessionViewHolder(val binding: ItemSessionBinding) :
        RecyclerView.ViewHolder(binding.root)

    val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Session>() {
        override fun areItemsTheSame(oldItem: Session, newItem: Session) =
            oldItem.time_price == newItem.time_price

        override fun areContentsTheSame(oldItem: Session, newItem: Session) = oldItem == newItem
    })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SessionViewHolder(
        ItemSessionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: SessionAdapter.SessionViewHolder, position: Int) {
        bind(holder, position)
    }

    override fun getItemCount() = differ.currentList.size

    private fun bind(holder: SessionViewHolder, position: Int) {
        val session = differ.currentList[position]
        holder.binding.apply {
            tvSession.text = session.session
            tvTimePrice.text = session.time_price
        }
    }
}