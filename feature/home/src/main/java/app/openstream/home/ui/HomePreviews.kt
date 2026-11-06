package app.openstream.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.openstream.core.common.AppError
import app.openstream.core.common.state.SectionState
import app.openstream.core.model.ThemeMode
import app.openstream.core.model.ThemeSettings
import app.openstream.designsystem.theme.OpenStreamTheme
import app.openstream.home.data.sampleHomeState

@Composable
private fun PreviewHome(state: HomeUiState) {
    OpenStreamTheme(
        paletteId = state.theme.paletteId,
        darkTheme = state.theme.mode == ThemeMode.DARK,
    ) {
        HomeScreen(
            state = state,
            onMovieClick = {},
            onFavoritesClick = {},
            onRetry = {},
            onTryAgain = {},
            onDismissOfflineSheet = {},
            onThemeChange = {},
        )
    }
}

@PreviewLightDark
@Preview(name = "Ocean", showSystemUi = true)
@Composable
private fun HomeContentPreview() = PreviewHome(sampleHomeState)

@Preview(name = "Ocean dark", showSystemUi = true)
@Composable
private fun HomeOceanDarkPreview() =
    PreviewHome(sampleHomeState.copy(theme = ThemeSettings(ThemeMode.DARK, "ocean")))

@Preview(name = "Loading", showSystemUi = true)
@Composable
private fun HomeLoadingPreview() = PreviewHome(HomeUiState(favoriteCount = 3))

@Preview(name = "Empty", showSystemUi = true)
@Composable
private fun HomeEmptyPreview() =
    PreviewHome(sampleHomeState.copy(popular = SectionState.Empty, nowPlaying = SectionState.Empty))

@Preview(name = "Top Rated failed", showSystemUi = true)
@Composable
private fun HomeOneErrorPreview() =
    PreviewHome(sampleHomeState.copy(topRated = SectionState.Error(AppError.Server)))

@Preview(name = "All failed", showSystemUi = true)
@Composable
private fun HomeAllErrorPreview() = PreviewHome(
    sampleHomeState.copy(
        popular = SectionState.Error(AppError.Network),
        topRated = SectionState.Error(AppError.Network),
        nowPlaying = SectionState.Error(AppError.RateLimited),
    ),
)

@Preview(name = "No connection sheet", showSystemUi = true)
@Composable
private fun HomeOfflineSheetPreview() = PreviewHome(
    sampleHomeState.copy(
        popular = SectionState.Error(AppError.Network),
        offlineSheetVisible = true,
    ),
)

@Preview(name = "No favorites", showSystemUi = true)
@Composable
private fun HomeNoFavoritesPreview() = PreviewHome(sampleHomeState.copy(favoriteCount = 0))
