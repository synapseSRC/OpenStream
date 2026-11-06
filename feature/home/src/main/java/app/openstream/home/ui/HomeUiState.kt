package app.openstream.home.ui

import app.openstream.core.common.state.SectionState
import app.openstream.core.model.Movie
import app.openstream.core.model.ThemeSettings

enum class HomeSection { Popular, TopRated, NowPlaying }

data class HomeUiState(
    val popular: SectionState<List<Movie>> = SectionState.Loading,
    val topRated: SectionState<List<Movie>> = SectionState.Loading,
    val nowPlaying: SectionState<List<Movie>> = SectionState.Loading,
    val offlineSheetVisible: Boolean = false,
    val favoriteCount: Int = 0,
    val theme: ThemeSettings = ThemeSettings(),
)
