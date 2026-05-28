package com.nikhilgreenbot.fitznipins.domain.repository

import android.net.Uri
import com.nikhilgreenbot.fitznipins.domain.model.FitzNiResult
import com.nikhilgreenbot.fitznipins.domain.model.Franchise
import com.nikhilgreenbot.fitznipins.domain.model.IdentifyResult
import com.nikhilgreenbot.fitznipins.domain.model.OfficialProduct
import com.nikhilgreenbot.fitznipins.domain.model.UserPin
import kotlinx.coroutines.flow.Flow

// ─────────────────────────────────────────────
// User's personal collection
// ─────────────────────────────────────────────
interface PinRepository {
    /** Continuous stream of the user's collection. */
    fun observeCollection(): Flow<List<UserPin>>

    /** Continuous stream of the user's wishlist. */
    fun observeWishlist(): Flow<List<UserPin>>

    /** Returns a single pin or null. */
    suspend fun getPinById(id: String): UserPin?

    /** Insert or update a pin (Room upsert). */
    suspend fun upsert(pin: UserPin): FitzNiResult<Unit>

    /** Delete a pin and its associated local photo files. */
    suspend fun delete(pinId: String): FitzNiResult<Unit>

    /** Full-text search across title, description, tags. */
    fun searchCollection(query: String): Flow<List<UserPin>>
}

// ─────────────────────────────────────────────
// Official Pin-Tastic catalog (Disney Store)
// ─────────────────────────────────────────────
interface CatalogRepository {
    /** Trigger a background refresh from your backend. */
    suspend fun refreshOfficialProducts(): FitzNiResult<Unit>

    /** Observe cached catalog, optionally filtered by franchise. */
    fun observeOfficialProducts(franchise: Franchise = Franchise.ALL): Flow<List<OfficialProduct>>

    /** Get a single product for detail screen. */
    suspend fun getProductById(id: String): OfficialProduct?

    /** Toggle wishlist status (stored locally). */
    suspend fun toggleWishlist(productId: String): FitzNiResult<Unit>
}

// ─────────────────────────────────────────────
// Pin identification API
// ─────────────────────────────────────────────
interface IdentifyRepository {
    /** Upload an image and get match candidates back. */
    suspend fun identify(imageUri: Uri): FitzNiResult<IdentifyResult>
}

// ─────────────────────────────────────────────
// App preferences (onboarding, settings)
// ─────────────────────────────────────────────
interface PreferencesRepository {
    val isOnboardingComplete: Flow<Boolean>
    suspend fun setOnboardingComplete()
    val lastCatalogSync: Flow<Long?>
    suspend fun setLastCatalogSync(epochMillis: Long)
}
