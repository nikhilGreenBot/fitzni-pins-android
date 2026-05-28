package com.nikhilgreenbot.fitznipins.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nikhilgreenbot.fitznipins.domain.model.PinLocation
import com.nikhilgreenbot.fitznipins.domain.model.TradeStatus

@Entity(tableName = "user_pins")
data class UserPinEntity(
    @PrimaryKey
    val id: String,

    val title: String,
    val description: String?,

    @ColumnInfo(name = "acquired_at")
    val acquiredAt: String?,              // LocalDate as ISO string

    val location: String?,                // PinLocation name

    @ColumnInfo(name = "photo_uris")
    val photoUris: String,                // JSON array of URIs

    val tags: String,                     // JSON array of tags

    @ColumnInfo(name = "official_product_id")
    val officialProductId: String?,

    @ColumnInfo(name = "external_catalog_id")
    val externalCatalogId: String?,

    @ColumnInfo(name = "is_wishlist")
    val isWishlist: Boolean,

    @ColumnInfo(name = "trade_status")
    val tradeStatus: String?,             // TradeStatus name

    @ColumnInfo(name = "created_at")
    val createdAt: Long,                  // Instant.epochSecond

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
)

@Entity(tableName = "official_products")
data class OfficialProductEntity(
    @PrimaryKey
    val id: String,

    val title: String,
    val price: String,

    @ColumnInfo(name = "image_url")
    val imageUrl: String,

    @ColumnInfo(name = "product_url")
    val productUrl: String,

    val franchise: String,                // Franchise name

    val badges: String,                   // JSON array of ProductBadge names

    val description: String?,

    @ColumnInfo(name = "is_in_wishlist")
    val isInWishlist: Boolean = false,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
)
