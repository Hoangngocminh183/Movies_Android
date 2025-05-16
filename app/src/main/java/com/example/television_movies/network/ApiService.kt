//*
package com.example.television_movies.network

import com.example.television_movies.Movie
import com.example.television_movies.MovieResponse

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val API_KEY = "de31b870b958936ab2702e760566a400" // API Key của bạn
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500" // w500 là kích thước ảnh
    }

    // Lấy danh sách phim phổ biến
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<MovieResponse> // Bây giờ MovieResponse sẽ được nhận diện

    // Lấy danh sách TV shows phổ biến (nếu bạn muốn hiển thị series như trong ảnh)
    @GET("tv/popular")
    suspend fun getPopularTvShows(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<MovieResponse> // MovieResponse dùng chung được vì cấu trúc tương tự


    // Lấy chi tiết một phim
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "en-US"
    ): Response<Movie> // Movie đã được import

    // Lấy chi tiết một TV show
    @GET("tv/{tv_id}")
    suspend fun getTvShowDetails(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = "en-US"
    ): Response<Movie> // Movie đã được import
}