package app.openstream.core.data.local

import app.openstream.core.model.ThemeMode
import app.openstream.core.model.ThemeSettings

fun ThemeSettingsEntity?.toSettings(): ThemeSettings {
    val default = ThemeSettings()
    if (this == null) return default
    return ThemeSettings(
        mode = ThemeMode.entries.firstOrNull { it.name == mode } ?: default.mode,
        paletteId = paletteId,
    )
}

fun ThemeSettings.toEntity(): ThemeSettingsEntity =
    ThemeSettingsEntity(mode = mode.name, paletteId = paletteId)
