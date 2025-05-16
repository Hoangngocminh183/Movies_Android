//*
package com.example.television_movies
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.app.DetailsSupportFragmentBackgroundController
import androidx.leanback.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.television_movies.network.ApiService
import com.example.television_movies.R

class VideoDetailsFragment : DetailsSupportFragment() {

    private val TAG = "VideoDetailsFragment"
    private var mSelectedMovie: Movie? = null

    private lateinit var mDetailsBackground: DetailsSupportFragmentBackgroundController
    private lateinit var mPresenterSelector: ClassPresenterSelector
    private lateinit var mAdapter: ArrayObjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")

        mDetailsBackground = DetailsSupportFragmentBackgroundController(this)

        mSelectedMovie = activity?.intent?.getSerializableExtra(DetailsActivity.MOVIE_ITEM) as Movie?

        if (mSelectedMovie != null) {
            mPresenterSelector = ClassPresenterSelector()
            val detailsPresenter = FullWidthDetailsOverviewRowPresenter(DetailsDescriptionPresenter())

            // Setup background
            detailsPresenter.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.detail_background) // Tạo color này
            )
            detailsPresenter.setActionsBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.detail_actions_background) // Tạo color này
            )


            detailsPresenter.onActionClickedListener = OnActionClickedListener { action ->
                // Xử lý khi action được click, ví dụ "Play" hoặc "Add to Watchlist"
                // if (action.id == 1L) { Toast.makeText(activity, "Play action", Toast.LENGTH_SHORT).show() }
            }

            mPresenterSelector.addClassPresenter(DetailsOverviewRow::class.java, detailsPresenter)
            // Có thể thêm các presenter khác cho các loại row khác (ví dụ: ListRow cho "Related Movies")

            mAdapter = ArrayObjectAdapter(mPresenterSelector)
            setupDetailsOverviewRow()
            adapter = mAdapter

            // Load backdrop image
            loadBackdropImage(mSelectedMovie!!)

        } else {
            Log.e(TAG, "Movie data not found!")
            // Đóng activity hoặc hiển thị lỗi
            activity?.finish()
        }
    }

    private fun setupDetailsOverviewRow() {
        val row = DetailsOverviewRow(mSelectedMovie)
        // Load poster image
        val posterUrl = if (!mSelectedMovie?.poster_path.isNullOrEmpty()) {
            ApiService.IMAGE_BASE_URL + mSelectedMovie!!.poster_path
        } else {
            null
        }

        if (posterUrl != null) {
            Glide.with(this)
                .asBitmap()
                .load(posterUrl)
                .error(R.drawable.movie_placeholder) // Placeholder nếu lỗi
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        row.setImageBitmap(requireContext(), resource)
                        mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size())
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        } else {
            row.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.movie_placeholder))
        }


        // Thêm actions (nút bấm)
        val actionAdapter = ArrayObjectAdapter()
        actionAdapter.add(
            Action(
                1, // ID của action
                "Watch Trailer", // Text cho action
                // "Subtext if any"
            )
        )
        actionAdapter.add(
            Action(
                2,
                "Add to Favorites"
            )
        )
        row.actionsAdapter = actionAdapter
        mAdapter.add(row)
    }

    private fun loadBackdropImage(movie: Movie) {
        val backdropUrl = if (!movie.backdrop_path.isNullOrEmpty()) {
            ApiService.IMAGE_BASE_URL + movie.backdrop_path
        } else if (!movie.poster_path.isNullOrEmpty()) { // Dùng poster nếu không có backdrop
            ApiService.IMAGE_BASE_URL + movie.poster_path
        } else {
            null
        }

        if (backdropUrl != null) {
            mDetailsBackground.enableParallax() // Hiệu ứng parallax cho background
            Glide.with(this)
                .asBitmap()
                .load(backdropUrl)
                .centerCrop()
                .error(R.drawable.default_background) // Tạo drawable này
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        mDetailsBackground.coverBitmap = resource
                        mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size())
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        } else {
            // Đặt một màu nền cố định khi không có ảnh
            mDetailsBackground.setSolidColor(ContextCompat.getColor(requireContext(), R.color.default_detail_background_color))

        }
    }
}