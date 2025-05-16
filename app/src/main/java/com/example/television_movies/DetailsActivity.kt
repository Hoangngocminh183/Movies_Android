package com.example.television_movies

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

class DetailsActivity : FragmentActivity() {

    companion object {
        const val MOVIE_ITEM = "movie_item"
        const val SHARED_ELEMENT_NAME = "hero" // Tên cho shared element transition
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details) // Tạo layout này

        if (savedInstanceState == null) {
            val fragment = VideoDetailsFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.details_fragment_container, fragment)
                .commitNow()
        }
    }
}
