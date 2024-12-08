package com.mobdeve.s14.abenoja_delacruz.bookcol.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobdeve.s14.abenoja_delacruz.bookcol.R
import com.mobdeve.s14.abenoja_delacruz.bookcol.models.BookResponseModel
import com.mobdeve.s14.abenoja_delacruz.bookcol.databinding.ItemFeedBookLayoutBinding

class FeedBookAdapter(
    private val data: List<BookResponseModel>,
    private val onBookClick: (BookResponseModel) -> Unit
) : RecyclerView.Adapter<FeedBookAdapter.FeedBookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedBookViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemFeedBookLayoutBinding.inflate(layoutInflater, parent, false)
        return FeedBookViewHolder(viewBinding, onBookClick)
    }

    override fun onBindViewHolder(holder: FeedBookViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    class FeedBookViewHolder(
        private val viewBinding: ItemFeedBookLayoutBinding,
        private val onBookClick: (BookResponseModel) -> Unit
    ) : RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(book: BookResponseModel) {
            with(viewBinding) {
                bookTitleTextView.text = book.title
                bookAuthorTextView.text = book.authors?.joinToString(", ") { it.key ?: "" }
//                bookPublisherTextView.text = book.publishers?.joinToString(", ")

                // Load book cover using Glide
                book.covers?.firstOrNull()?.let { coverId ->
                    val coverUrl = "https://covers.openlibrary.org/b/id/$coverId-M.jpg"
                    Glide.with(root.context)
                        .load(coverUrl)
                        .placeholder(R.drawable.bg_login_background)
                        .error(R.drawable.bg_login_background)
                        .into(bookImageView)
                } ?: run {
                    bookImageView.setImageResource(R.drawable.bg_login_background)
                }

                // Set click listener
                root.setOnClickListener { onBookClick(book) }
            }
        }
    }
}
