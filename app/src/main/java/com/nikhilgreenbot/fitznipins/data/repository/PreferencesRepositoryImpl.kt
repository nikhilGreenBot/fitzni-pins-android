package com.nikhilgreenbot.fitznipins.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.nikhilgreenbot.fitznipins.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : PreferencesRepository {

    private object Keys {
        val ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")
        val LAST_CATALOG_SYNC   = longPreferencesKey("last_catalog_sync")
    }

    override val isOnboardingComplete: Flow<Boolean>
        get() = dataStore.data.map { it[Keys.ONBOARDING_COMPLETE] ?: false }

    override suspend fun setOnboardingComplete() {
        dataStore.edit { it[Keys.ONBOARDING_COMPLETE] = true }
    }

    override val lastCatalogSync: Flow<Long?>
        get() = dataStore.data.map { it[Keys.LAST_CATALOG_SYNC] }

    override suspend fun setLastCatalogSync(epochMillis: Long) {
        dataStore.edit { it[Keys.LAST_CATALOG_SYNC] = epochMillis }
    }
}
