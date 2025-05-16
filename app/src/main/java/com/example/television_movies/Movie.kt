//*
package com.example.television_movies
import java.io.Serializable

data class Movie(
    val id: Int,
    val title: String?,
    val name: String?, // For TV shows, title might be under 'name'
    val overview: String?,
    val poster_path: String?,
    val backdrop_path: String?,
    val vote_average: Double?
) : Serializable { // Implement Serializable để truyền qua Intent
    // Helper để lấy title, ưu tiên 'title' rồi đến 'name'
    fun getDisplayTitle(): String {
        return title ?: name ?: "No Title"
    }
}

data class MovieResponse(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)