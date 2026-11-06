package app.openstream.core.data.repository

import app.openstream.core.model.Movie
import app.openstream.core.model.MovieDetail
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun observeAll(): Flow<List<Movie>>
    fun observeCount(): Flow<Int>
    fun observeIsFavorite(id: Int): Flow<Boolean>
    suspend fun toggle(movie: MovieDetail): Boolean
    suspend fun remove(id: Int)
}
