package app.openstream.core.data.repository

import app.openstream.core.data.local.ThemeDao
import app.openstream.core.data.local.toEntity
import app.openstream.core.data.local.toSettings
import app.openstream.core.model.ThemeSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemeRepositoryImpl(
    private val dao: ThemeDao,
) : ThemeRepository {

    override val settings: Flow<ThemeSettings> = dao.observe().map { it.toSettings() }

    override suspend fun update(settings: ThemeSettings) = dao.save(settings.toEntity())
}
