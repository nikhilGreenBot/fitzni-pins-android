package com.nikhilgreenbot.fitznipins.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OfficialProductDto(
    val id: String,
    val title: String,
    val price: String,
    @SerialName("image_url")  val imageUrl: String,
    @SerialName("product_url") val productUrl: String,
    val franchise: String,
    val badges: List<String> = emptyList(),
    val description: String? = null,
    @SerialName("updated_at") val updatedAt: Long,
)

/** Product metadata embedded in Disney Store PLP tiles (Tealium analytics). */
@Serializable
data class TealiumProductDto(
    val id: String,
    val name: String,
    val price: String,
    @SerialName("original_price") val originalPrice: String? = null,
    val message: String? = null,
    val availability: String? = null,
    @SerialName("pims_character_name") val characterName: String? = null,
    val category: String? = null,
)

@Serializable
data class CatalogResponseDto(
    val products: List<OfficialProductDto>,
    val total: Int,
)

@Serializable
data class IdentifyResponseDto(
    val candidates: List<PinMatchDto>,
    val disclaimer: String,
    @SerialName("processed_at") val processedAt: Long,
)

@Serializable
data class PinMatchDto(
    @SerialName("pin_id")       val pinId: String,
    val title: String,
    val confidence: Float,
    @SerialName("thumbnail_url") val thumbnailUrl: String? = null,
    val source: String = "OWNED_CATALOG",
)
