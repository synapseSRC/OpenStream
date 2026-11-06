package app.openstream.core.data.repository

import app.openstream.core.common.AppError
import app.openstream.core.common.AppResult
import app.openstream.core.common.map
import app.openstream.core.data.mapper.toMovie
import app.openstream.core.data.mapper.toMovieDetail
import app.openstream.core.data.mapper.toReview
import app.openstream.core.data.network.safeApiCall
import app.openstream.core.data.network.service.ApiService
import app.openstream.core.model.Movie
import app.openstream.core.model.MovieDetail
import app.openstream.core.model.Review

class MovieRepositoryImpl(
    private val api: ApiService,
) : MovieRepository {
    override suspend fun popular(): AppResult<List<Movie>> =
        safeApiCall { api.getPopularMovies() }.map { list -> list.results.map { it.toMovie() } }

    override suspend fun topRated(): AppResult<List<Movie>> =
        safeApiCall { api.getTopRatedMovies() }.map { list -> list.results.map { it.toMovie() } }

    override suspend fun nowPlaying(): AppResult<List<Movie>> =
        safeApiCall { api.getNowPlayingMovies() }.map { list -> list.results.map { it.toMovie() } }

    override suspend fun movieDetail(id: Int): AppResult<MovieDetail> =
        safeApiCall { api.getMovieDetail(id) }.map { it.toMovieDetail() }

    override suspend fun reviews(id: Int): AppResult<List<Review>> {
        val result =
            safeApiCall { api.getReviews(id) }.map { page -> page.results.map { it.toReview() } }
        // A 404 on reviews means there are none. Only the movie call treats 404 as an error.
        return if (result is AppResult.Failure && result.error == AppError.NotFound) {
            AppResult.Success(emptyList())
        } else {
            result
        }
    }

}