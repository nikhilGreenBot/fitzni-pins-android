package com.nikhilgreenbot.fitznipins.data.mapper

import com.nikhilgreenbot.fitznipins.data.local.entity.OfficialProductEntity
import com.nikhilgreenbot.fitznipins.data.local.entity.UserPinEntity
import com.nikhilgreenbot.fitznipins.domain.model.Franchise
import com.nikhilgreenbot.fitznipins.domain.model.OfficialProduct
import com.nikhilgreenbot.fitznipins.domain.model.PinLocation
import com.nikhilgreenbot.fitznipins.domain.model.ProductBadge
import com.nikhilgreenbot.fitznipins.domain.model.TradeStatus
import com.nikhilgreenbot.fitznipins.domain.model.UserPin
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant
import java.time.LocalDate

private val json = Json { ignoreUnknownKeys = true }

// ─────────────────────────────────────────────
// UserPin ↔ UserPinEntity
// ─────────────────────────────────────────────

fun UserPinEntity.toDomain(): UserPin = UserPin(
    id = id,
    title = title,
    description = description,
    acquiredAt = acquiredAt?.let { LocalDate.parse(it) },
    location = location?.let { runCatching { PinLocation.valueOf(it) }.getOrNull() },
    photoUris = runCatching { json.decodeFromString<List<String>>(photoUris) }.getOrDefault(emptyList()),
    tags = runCatching { json.decodeFromString<List<String>>(tags) }.getOrDefault(emptyList()),
    officialProductId = officialProductId,
    externalCatalogId = externalCatalogId,
    isWishlist = isWishlist,
    tradeStatus = tradeStatus?.let { runCatching { TradeStatus.valueOf(it) }.getOrNull() },
    createdAt = Instant.ofEpochSecond(createdAt),
    updatedAt = Instant.ofEpochSecond(updatedAt),
)

fun UserPin.toEntity(): UserPinEntity = UserPinEntity(
    id = id,
    title = title,
    description = description,
    acquiredAt = acquiredAt?.toString(),
    location = location?.name,
    photoUris = json.encodeToString(photoUris),
    tags = json.encodeToString(tags),
    officialProductId = officialProductId,
    externalCatalogId = externalCatalogId,
    isWishlist = isWishlist,
    tradeStatus = tradeStatus?.name,
    createdAt = createdAt.epochSecond,
    updatedAt = updatedAt.epochSecond,
)

// ─────────────────────────────────────────────
// OfficialProduct ↔ OfficialProductEntity
// ─────────────────────────────────────────────

fun OfficialProductEntity.toDomain(): OfficialProduct = OfficialProduct(
    id = id,
    title = title,
    price = price,
    imageUrl = imageUrl,
    productUrl = productUrl,
    franchise = runCatching { Franchise.valueOf(franchise) }.getOrDefault(Franchise.DISNEY),
    badges = runCatching {
        json.decodeFromString<List<String>>(badges)
            .mapNotNull { runCatching { ProductBadge.valueOf(it) }.getOrNull() }
    }.getOrDefault(emptyList()),
    description = description,
    isInWishlist = isInWishlist,
    updatedAt = Instant.ofEpochSecond(updatedAt),
)

fun OfficialProduct.toEntity(): OfficialProductEntity = OfficialProductEntity(
    id = id,
    title = title,
    price = price,
    imageUrl = imageUrl,
    productUrl = productUrl,
    franchise = franchise.name,
    badges = json.encodeToString(badges.map { it.name }),
    description = description,
    isInWishlist = isInWishlist,
    updatedAt = updatedAt.epochSecond,
)
