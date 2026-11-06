package app.openstream.core.di

import app.openstream.core.data.local.FavoriteDao
import app.openstream.core.data.local.ThemeDao
import app.openstream.core.data.network.service.ApiService
import app.openstream.core.data.repository.FavoriteRepository
import app.openstream.core.data.repository.FavoriteRepositoryImpl
import app.openstream.core.data.repository.MovieRepository
import app.openstream.core.data.repository.MovieRepositoryImpl
import app.openstream.core.data.repository.ThemeRepository
import app.openstream.core.data.repository.ThemeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun bindMovieRepository(apiService: ApiService): MovieRepository {
        return MovieRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideFavoriteRepository(dao: FavoriteDao): FavoriteRepository {
        return FavoriteRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideThemeRepository(dao: ThemeDao): ThemeRepository {
        return ThemeRepositoryImpl(dao)
    }
}
