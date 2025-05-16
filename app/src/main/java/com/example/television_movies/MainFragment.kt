package com.example.television_movies

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.lifecycleScope
import com.example.television_movies.network.RetrofitClient
import kotlinx.coroutines.launch
import java.io.IOException // Import IOException

class MainFragment : BrowseSupportFragment() {

    private val TAG = "MainFragment"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupUIElements()
        loadRows()
        setupEventListeners()
    }

    private fun setupUIElements() {
        title = getString(R.string.browse_title) // Tạo string resource này
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        brandColor = ContextCompat.getColor(requireActivity(), R.color.fastlane_background) // Tạo color resource này
        // searchAffordanceColor = ContextCompat.getColor(requireActivity(), R.color.search_opaque) // Tùy chỉnh màu nút search
    }

    private fun loadRows() {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val cardPresenter = CardPresenter()

        // --- Hàng Phim Phổ Biến ---
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.getPopularMovies()
                if (response.isSuccessful) {
                    val movies: List<Movie>? = response.body()?.results // Chỉ định kiểu rõ ràng ở đây
                    if (!movies.isNullOrEmpty()) { // Kiểm tra null và rỗng
                        val listRowAdapter = ArrayObjectAdapter(cardPresenter)
                        movies.forEach { movie: Movie -> listRowAdapter.add(movie) } // Chỉ định kiểu cho movie
                        val header = HeaderItem(0, "Popular Movies")
                        rowsAdapter.add(ListRow(header, listRowAdapter))
                    }
                } else {
                    Log.e(TAG, "Error fetching popular movies: ${response.code()}")
                    Toast.makeText(activity, "Error fetching movies: ${response.message()}", Toast.LENGTH_LONG).show()
                }
            } catch (e: IOException) {
                Log.e(TAG, "Network error fetching popular movies", e)
                Toast.makeText(activity, "Network error: ${e.message}", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching popular movies", e)
                Toast.makeText(activity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }

            // --- Hàng TV Shows Phổ Biến (Tùy chọn, giống trong ảnh) ---
            try {
                val tvResponse = RetrofitClient.instance.getPopularTvShows()
                if (tvResponse.isSuccessful) {
                    val tvShows: List<Movie>? = tvResponse.body()?.results // Chỉ định kiểu rõ ràng ở đây
                    if (!tvShows.isNullOrEmpty()) { // Kiểm tra null và rỗng
                        val tvListRowAdapter = ArrayObjectAdapter(cardPresenter)
                        tvShows.forEach { tvShow: Movie -> tvListRowAdapter.add(tvShow) } // Chỉ định kiểu cho tvShow
                        val tvHeader = HeaderItem(1, "Popular TV Shows") // ID khác nhau
                        rowsAdapter.add(ListRow(tvHeader, tvListRowAdapter))
                    }
                } else {
                    Log.e(TAG, "Error fetching popular TV shows: ${tvResponse.code()}")
                    Toast.makeText(activity, "Error fetching TV shows: ${tvResponse.message()}", Toast.LENGTH_LONG).show()
                }
            } catch (e: IOException) {
                Log.e(TAG, "Network error fetching popular TV shows", e)
                Toast.makeText(activity, "Network error: ${e.message}", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching popular TV shows", e)
                Toast.makeText(activity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }

            adapter = rowsAdapter
        }
    }


    private fun setupEventListeners() {
        onItemViewClickedListener = ItemViewClickedListener()
        // onItemViewSelectedListener = ItemViewSelectedListener() // Nếu cần
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder,
            item: Any,
            rowViewHolder: RowPresenter.ViewHolder,
            row: Row
        ) {
            if (item is Movie) {
                Log.d(TAG, "Item: " + item.getDisplayTitle())
                val intent = Intent(activity!!, DetailsActivity::class.java)
                intent.putExtra(DetailsActivity.MOVIE_ITEM, item) // Truyền cả object Movie

                val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity!!,
                    (itemViewHolder.view as ImageCardView).mainImageView,
                    DetailsActivity.SHARED_ELEMENT_NAME
                ).toBundle()
                startActivity(intent, bundle)
            }
        }
    }
}