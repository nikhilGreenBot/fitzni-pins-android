package com.nikhilgreenbot.fitznipins.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.nikhilgreenbot.fitznipins.data.local.FitzNiDatabase
import com.nikhilgreenbot.fitznipins.data.local.dao.OfficialProductDao
import com.nikhilgreenbot.fitznipins.data.local.dao.UserPinDao
import com.nikhilgreenbot.fitznipins.data.repository.CatalogRepositoryImpl
import com.nikhilgreenbot.fitznipins.data.repository.IdentifyRepositoryImpl
import com.nikhilgreenbot.fitznipins.data.repository.PreferencesRepositoryImpl
import com.nikhilgreenbot.fitznipins.data.repository.PinRepositoryImpl
import com.nikhilgreenbot.fitznipins.domain.repository.CatalogRepository
import com.nikhilgreenbot.fitznipins.domain.repository.IdentifyRepository
import com.nikhilgreenbot.fitznipins.domain.repository.PreferencesRepository
import com.nikhilgreenbot.fitznipins.domain.repository.PinRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(name = "fitzni_prefs")

// ─────────────────────────────────────────────
// Database module
// ─────────────────────────────────────────────
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FitzNiDatabase =
        Room.databaseBuilder(
            context,
            FitzNiDatabase::class.java,
            FitzNiDatabase.DATABASE_NAME
        ).build()

    @Provides
    fun provideUserPinDao(db: FitzNiDatabase): UserPinDao = db.userPinDao()

    @Provides
    fun provideOfficialProductDao(db: FitzNiDatabase): OfficialProductDao = db.officialProductDao()

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore
}

// ─────────────────────────────────────────────
// Repository bindings module
// ─────────────────────────────────────────────
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPinRepository(impl: PinRepositoryImpl): PinRepository

    @Binds
    @Singleton
    abstract fun bindCatalogRepository(impl: CatalogRepositoryImpl): CatalogRepository

    @Binds
    @Singleton
    abstract fun bindIdentifyRepository(impl: IdentifyRepositoryImpl): IdentifyRepository

    @Binds
    @Singleton
    abstract fun bindPreferencesRepository(impl: PreferencesRepositoryImpl): PreferencesRepository
}
