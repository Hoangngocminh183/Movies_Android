//*
package com.example.television_movies
import androidx.leanback.widget.AbstractDetailsDescriptionPresenter

class DetailsDescriptionPresenter : AbstractDetailsDescriptionPresenter() {
    override fun onBindDescription(viewHolder: ViewHolder, item: Any) {
        val movie = item as Movie
        viewHolder.title.text = movie.getDisplayTitle()
        viewHolder.subtitle.text = "Rating: ${movie.vote_average ?: "N/A"}" // Ví dụ subtitle
        viewHolder.body.text = movie.overview ?: "No overview available."
    }
}