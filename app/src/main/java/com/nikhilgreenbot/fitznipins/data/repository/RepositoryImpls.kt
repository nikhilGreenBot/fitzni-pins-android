package com.nikhilgreenbot.fitznipins.data.repository

import android.net.Uri
import com.nikhilgreenbot.fitznipins.data.local.dao.OfficialProductDao
import com.nikhilgreenbot.fitznipins.data.local.dao.UserPinDao
import com.nikhilgreenbot.fitznipins.data.mapper.toDomain
import com.nikhilgreenbot.fitznipins.data.mapper.toEntity
import com.nikhilgreenbot.fitznipins.data.remote.MockCatalogSeed
import com.nikhilgreenbot.fitznipins.domain.model.FitzNiResult
import com.nikhilgreenbot.fitznipins.domain.model.Franchise
import com.nikhilgreenbot.fitznipins.domain.model.IdentifyResult
import com.nikhilgreenbot.fitznipins.domain.model.MatchSource
import com.nikhilgreenbot.fitznipins.domain.model.OfficialProduct
import com.nikhilgreenbot.fitznipins.domain.model.PinMatch
import com.nikhilgreenbot.fitznipins.domain.model.UserFacingError
import com.nikhilgreenbot.fitznipins.domain.model.UserPin
import com.nikhilgreenbot.fitznipins.domain.repository.CatalogRepository
import com.nikhilgreenbot.fitznipins.domain.repository.IdentifyRepository
import com.nikhilgreenbot.fitznipins.domain.repository.PinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PinRepositoryImpl @Inject constructor(
    private val dao: UserPinDao,
) : PinRepository {

    override fun observeCollection(): Flow<List<UserPin>> =
        dao.observeCollection().map { entities -> entities.map { it.toDomain() } }

    override fun observeWishlist(): Flow<List<UserPin>> =
        dao.observeWishlist().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getPinById(id: String): UserPin? =
        dao.getById(id)?.toDomain()

    override suspend fun upsert(pin: UserPin): FitzNiResult<Unit> = runCatching {
        dao.upsert(pin.toEntity())
    }.fold(
        onSuccess = { FitzNiResult.Success(Unit) },
        onFailure = { FitzNiResult.Error(UserFacingError.Unknown) }
    )

    override suspend fun delete(pinId: String): FitzNiResult<Unit> = runCatching {
        dao.deleteById(pinId)
    }.fold(
        onSuccess = { FitzNiResult.Success(Unit) },
        onFailure = { FitzNiResult.Error(UserFacingError.Unknown) }
    )

    override fun searchCollection(query: String): Flow<List<UserPin>> =
        dao.search(query).map { it.map { entity -> entity.toDomain() } }
}

// ─────────────────────────────────────────────

@Singleton
class CatalogRepositoryImpl @Inject constructor(
    private val dao: OfficialProductDao,
) : CatalogRepository {

    override suspend fun refreshOfficialProducts(): FitzNiResult<Unit> = runCatching {
        // Phase B: replace with Retrofit API call
        // For now, seed the DB with mock data on first run
        if (dao.count() == 0) {
            val entities = MockCatalogSeed.products.map { dto ->
                com.nikhilgreenbot.fitznipins.domain.model.OfficialProduct(
                    id = dto.id,
                    title = dto.title,
                    price = dto.price,
                    imageUrl = dto.imageUrl,
                    productUrl = dto.productUrl,
                    franchise = runCatching { Franchise.valueOf(dto.franchise) }.getOrDefault(Franchise.DISNEY),
                    description = dto.description,
                    updatedAt = Instant.ofEpochSecond(dto.updatedAt),
                ).toEntity()
            }
            dao.upsertAll(entities)
        }
    }.fold(
        onSuccess = { FitzNiResult.Success(Unit) },
        onFailure = { FitzNiResult.Error(UserFacingError.Unknown) }
    )

    override fun observeOfficialProducts(franchise: Franchise): Flow<List<OfficialProduct>> =
        if (franchise == Franchise.ALL) {
            dao.observeAll().map { it.map { e -> e.toDomain() } }
        } else {
            dao.observeByFranchise(franchise.name).map { it.map { e -> e.toDomain() } }
        }

    override suspend fun getProductById(id: String): OfficialProduct? =
        dao.getById(id)?.toDomain()

    override suspend fun toggleWishlist(productId: String): FitzNiResult<Unit> = runCatching {
        dao.toggleWishlist(productId)
    }.fold(
        onSuccess = { FitzNiResult.Success(Unit) },
        onFailure = { FitzNiResult.Error(UserFacingError.Unknown) }
    )
}

// ─────────────────────────────────────────────

@Singleton
class IdentifyRepositoryImpl @Inject constructor() : IdentifyRepository {

    override suspend fun identify(imageUri: Uri): FitzNiResult<IdentifyResult> {
        // Phase C: replace with real multipart Retrofit upload
        // Stub: simulate network delay and return mock candidates
        kotlinx.coroutines.delay(2_000)
        return FitzNiResult.Success(
            IdentifyResult(
                candidates = listOf(
                    PinMatch("pin-001", "Mickey Mouse Icon Pin – Glitter",    0.87f, null, MatchSource.OWNED_CATALOG),
                    PinMatch("pin-007", "Lilo & Stitch Surfboard Set",         0.61f, null, MatchSource.OWNED_CATALOG),
                    PinMatch("pin-004", "Mandalorian & Grogu Boho Pin",        0.44f, null, MatchSource.USER_SUBMITTED),
                ),
                processedAt = Instant.now(),
            )
        )
    }
}
