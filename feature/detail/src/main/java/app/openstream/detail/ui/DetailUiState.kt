package app.openstream.detail.ui

import app.openstream.core.common.state.SectionState
import app.openstream.core.model.MovieDetail
import app.openstream.core.model.Review

/**
 * Everything Detail draws. The movie and its reviews are separate calls, so each has its own
 * state and one can fail without hiding the other.
 */
data class DetailUiState(
    val movie: SectionState<MovieDetail> = SectionState.Loading,
    val reviews: SectionState<List<Review>> = SectionState.Loading,
    val isFavorite: Boolean = false,
    val shareSheetVisible: Boolean = false,
    val offlineSheetVisible: Boolean = false,
)
