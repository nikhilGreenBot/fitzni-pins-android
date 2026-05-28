package com.nikhilgreenbot.fitznipins.domain.usecase

import android.net.Uri
import com.nikhilgreenbot.fitznipins.domain.model.FitzNiResult
import com.nikhilgreenbot.fitznipins.domain.model.Franchise
import com.nikhilgreenbot.fitznipins.domain.model.IdentifyResult
import com.nikhilgreenbot.fitznipins.domain.model.OfficialProduct
import com.nikhilgreenbot.fitznipins.domain.model.UserPin
import com.nikhilgreenbot.fitznipins.domain.repository.CatalogRepository
import com.nikhilgreenbot.fitznipins.domain.repository.IdentifyRepository
import com.nikhilgreenbot.fitznipins.domain.repository.PinRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// ─────────────────────────────────────────────
// Collection use cases
// ─────────────────────────────────────────────

class ObserveCollectionUseCase @Inject constructor(
    private val repository: PinRepository,
) {
    operator fun invoke(): Flow<List<UserPin>> = repository.observeCollection()
}

class ObserveWishlistUseCase @Inject constructor(
    private val repository: PinRepository,
) {
    operator fun invoke(): Flow<List<UserPin>> = repository.observeWishlist()
}

class UpsertPinUseCase @Inject constructor(
    private val repository: PinRepository,
) {
    suspend operator fun invoke(pin: UserPin): FitzNiResult<Unit> {
        // Domain validation
        if (pin.title.isBlank()) {
            return FitzNiResult.Error(
                com.nikhilgreenbot.fitznipins.domain.model.UserFacingError.Validation(
                    field = "title",
                    reason = "Title cannot be empty"
                )
            )
        }
        return repository.upsert(pin)
    }
}

class DeletePinUseCase @Inject constructor(
    private val repository: PinRepository,
) {
    suspend operator fun invoke(pinId: String): FitzNiResult<Unit> =
        repository.delete(pinId)
}

class SearchCollectionUseCase @Inject constructor(
    private val repository: PinRepository,
) {
    operator fun invoke(query: String): Flow<List<UserPin>> =
        repository.searchCollection(query)
}

// ─────────────────────────────────────────────
// Catalog / Pin-Tastic use cases
// ─────────────────────────────────────────────

class ObservePinTasticProductsUseCase @Inject constructor(
    private val repository: CatalogRepository,
) {
    operator fun invoke(franchise: Franchise = Franchise.ALL): Flow<List<OfficialProduct>> =
        repository.observeOfficialProducts(franchise)
}

class RefreshCatalogUseCase @Inject constructor(
    private val repository: CatalogRepository,
) {
    suspend operator fun invoke(): FitzNiResult<Unit> =
        repository.refreshOfficialProducts()
}

class GetProductByIdUseCase @Inject constructor(
    private val repository: CatalogRepository,
) {
    suspend operator fun invoke(id: String): OfficialProduct? =
        repository.getProductById(id)
}

class ToggleProductWishlistUseCase @Inject constructor(
    private val repository: CatalogRepository,
) {
    suspend operator fun invoke(productId: String): FitzNiResult<Unit> =
        repository.toggleWishlist(productId)
}

// ─────────────────────────────────────────────
// Identify use case
// ─────────────────────────────────────────────

class IdentifyPinUseCase @Inject constructor(
    private val repository: IdentifyRepository,
) {
    suspend operator fun invoke(imageUri: Uri): FitzNiResult<IdentifyResult> =
        repository.identify(imageUri)
}
