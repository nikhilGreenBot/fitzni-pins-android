package com.nikhilgreenbot.fitznipins.domain.model

import java.time.Instant
import java.time.LocalDate

// ─────────────────────────────────────────────
// User's personal collection pin
// ─────────────────────────────────────────────

data class UserPin(
    val id: String,
    val title: String,
    val description: String? = null,
    val acquiredAt: LocalDate? = null,
    val location: PinLocation? = null,
    val photoUris: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val officialProductId: String? = null,
    val externalCatalogId: String? = null,   // optional user-entered PinPics # — display only
    val isWishlist: Boolean = false,
    val tradeStatus: TradeStatus? = null,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
)

enum class PinLocation {
    PARK, STORE, EVENT, ONLINE, TRADE, GIFT
}

enum class TradeStatus {
    NOT_FOR_TRADE, OPEN_TO_TRADE
}

// ─────────────────────────────────────────────
// Official Pin-Tastic products (Disney Store)
// ─────────────────────────────────────────────

data class OfficialProduct(
    val id: String,
    val title: String,
    val price: String,
    val imageUrl: String,
    val productUrl: String,
    val franchise: Franchise,
    val badges: List<ProductBadge> = emptyList(),
    val description: String? = null,
    val isInWishlist: Boolean = false,
    val updatedAt: Instant = Instant.now(),
)

enum class Franchise(val displayName: String) {
    DISNEY("Disney"),
    PIXAR("Pixar"),
    MARVEL("Marvel"),
    STAR_WARS("Star Wars"),
    ALL("All")
}

enum class ProductBadge(val label: String) {
    NEW("New"),
    LIMITED_RELEASE("Limited Release"),
    SOLD_OUT("Sold Out"),
    PRE_ORDER("Pre-Order"),
    PIN_TASTIC_TUESDAY("Pin-Tastic Tuesday"),
}

// ─────────────────────────────────────────────
// Identification result
// ─────────────────────────────────────────────

data class IdentifyResult(
    val candidates: List<PinMatch>,
    val processedAt: Instant,
)

data class PinMatch(
    val pinId: String,
    val title: String,
    val confidence: Float,       // 0f..1f
    val thumbnailUrl: String? = null,
    val source: MatchSource,
)

enum class MatchSource {
    OWNED_CATALOG, USER_SUBMITTED
}
