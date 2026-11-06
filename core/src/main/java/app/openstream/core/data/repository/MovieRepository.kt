package app.openstream.core.data.repository

import app.openstream.core.common.AppResult
import app.openstream.core.model.Movie
import app.openstream.core.model.MovieDetail
import app.openstream.core.model.Review

interface MovieRepository {
    suspend fun popular(): AppResult<List<Movie>>
    suspend fun topRated(): AppResult<List<Movie>>
    suspend fun nowPlaying(): AppResult<List<Movie>>
    suspend fun movieDetail(id: Int): AppResult<MovieDetail>

    suspend fun reviews(id: Int): AppResult<List<Review>>
}