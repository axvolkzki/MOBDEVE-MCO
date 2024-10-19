package com.mobdeve.s14.amelia_delacruz.bookcol

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s14.amelia_delacruz.bookcol.databinding.ItemBookFeedBinding
import com.mobdeve.s14.amelia_delacruz.bookcol.databinding.ItemBookLayoutBinding

class MyAdapter(private val data: ArrayList<BookModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_TYPE_FEED = 0
        const val VIEW_TYPE_LAYOUT = 1
    }

    override fun getItemViewType(position: Int): Int {
        // Determine which layout to use based on your data or criteria
        return if (data[position].isFeedType) {
            VIEW_TYPE_FEED
        } else {
            VIEW_TYPE_LAYOUT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_FEED -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val viewBinding = ItemBookFeedBinding.inflate(layoutInflater, parent, false)
                FeedViewHolder(viewBinding)
            }
            VIEW_TYPE_LAYOUT -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val viewBinding = ItemBookLayoutBinding.inflate(layoutInflater, parent, false)
                LayoutViewHolder(viewBinding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FeedViewHolder -> holder.bind(data[position])
            is LayoutViewHolder -> holder.bind(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

