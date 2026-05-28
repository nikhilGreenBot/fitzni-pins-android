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
