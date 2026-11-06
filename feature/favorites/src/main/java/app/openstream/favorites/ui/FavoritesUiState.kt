package app.openstream.favorites.ui

import app.openstream.core.common.state.SectionState
import app.openstream.core.model.Movie

data class FavoritesUiState(
    val favorites: SectionState<List<Movie>> = SectionState.Loading,
)
