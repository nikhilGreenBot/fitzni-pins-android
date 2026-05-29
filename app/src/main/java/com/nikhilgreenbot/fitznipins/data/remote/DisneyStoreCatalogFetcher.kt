package com.nikhilgreenbot.fitznipins.data.remote

import com.nikhilgreenbot.fitznipins.data.remote.dto.OfficialProductDto
import com.nikhilgreenbot.fitznipins.data.remote.dto.TealiumProductDto
import com.nikhilgreenbot.fitznipins.domain.model.Franchise
import com.nikhilgreenbot.fitznipins.domain.model.ProductBadge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Fetches the public Disney Store pins listing and extracts product metadata
 * embedded in each tile's `data-tealium-productstring` attribute plus CDN image URLs.
 */
@Singleton
class DisneyStoreCatalogFetcher @Inject constructor(
    private val okHttpClient: OkHttpClient,
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun fetch(): List<OfficialProductDto> = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(PINS_LISTING_URL)
            .header("Accept", "text/html,application/xhtml+xml")
            .header("Accept-Language", "en-US,en;q=0.9")
            .build()

        val response = okHttpClient.newCall(request).execute()
        if (!response.isSuccessful) {
            Timber.w("Disney Store catalog HTTP ${response.code}")
            return@withContext emptyList()
        }

        val html = response.body?.string().orEmpty()
        if (html.isBlank()) return@withContext emptyList()

        parseProducts(html).also { products ->
            Timber.d("Disney Store catalog: parsed ${products.size} pins")
        }
    }

    internal fun parseProducts(html: String): List<OfficialProductDto> {
        val tiles = html.split(TILE_MARKER).filter { it.contains(TEALIUM_ATTR) }
        val seen = mutableSetOf<String>()

        return tiles.mapNotNull { tile -> parseTile(tile)?.takeIf { seen.add(it.id) } }
    }

    private fun parseTile(tile: String): OfficialProductDto? {
        val jsonEncoded = TEALIUM_REGEX.find(tile)?.groupValues?.get(1) ?: return null
        val tealium = runCatching {
            json.decodeFromString<TealiumProductDto>(decodeHtmlEntities(jsonEncoded))
        }.getOrElse {
            Timber.w(it, "Failed to parse tealium JSON")
            return null
        }

        val imageSku = IMAGE_SKU_REGEX.find(tile)?.groupValues?.get(1) ?: return null
        val relativePath = PRODUCT_LINK_REGEX.find(tile)?.groupValues?.get(1) ?: return null

        val title = decodeHtmlEntities(tealium.name).trim()
        if (title.isBlank()) return null

        return OfficialProductDto(
            id = tealium.id,
            title = title,
            price = formatPrice(tealium.price),
            imageUrl = buildImageUrl(imageSku),
            productUrl = "$BASE_URL$relativePath",
            franchise = inferFranchise(title, tealium.characterName).name,
            badges = inferBadges(title, tealium.message, tealium.availability),
            description = null,
            updatedAt = Instant.now().epochSecond,
        )
    }

    private fun formatPrice(price: String): String =
        if (price.startsWith("$")) price else "$$price"

    private fun buildImageUrl(sku: String): String =
        "$IMAGE_CDN$sku?fmt=jpeg&qlt=90&wid=400&hei=400"

    private fun inferFranchise(title: String, characterName: String?): Franchise {
        val haystack = "$title ${characterName.orEmpty()}"
        return when {
            haystack.contains("Star Wars", ignoreCase = true) -> Franchise.STAR_WARS
            haystack.contains("Marvel", ignoreCase = true) ||
                haystack.contains("Spider-Man", ignoreCase = true) ||
                haystack.contains("Miles Morales", ignoreCase = true) -> Franchise.MARVEL
            haystack.contains("Pixar", ignoreCase = true) ||
                listOf("Incredibles", "WALL", "Toy Story", "Monsters", "Up!", "Finding Nemo", "Inside Out")
                    .any { haystack.contains(it, ignoreCase = true) } -> Franchise.PIXAR
            else -> Franchise.DISNEY
        }
    }

    private fun inferBadges(
        title: String,
        message: String?,
        availability: String?,
    ): List<String> = buildList {
        if (message.equals("new", ignoreCase = true)) add(ProductBadge.NEW.name)
        if (title.contains("Limited Release", ignoreCase = true) ||
            title.contains("Limited Edition", ignoreCase = true)
        ) {
            add(ProductBadge.LIMITED_RELEASE.name)
        }
        if (title.contains("Pre-Order", ignoreCase = true)) add(ProductBadge.PRE_ORDER.name)
        if (availability?.contains("out_of_stock", ignoreCase = true) == true) {
            add(ProductBadge.SOLD_OUT.name)
        }
    }

    private fun decodeHtmlEntities(value: String): String = value
        .replace("&quot;", "\"")
        .replace("&#34;", "\"")
        .replace("&amp;", "&")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
        .replace("&ndash;", "–")
        .replace("&mdash;", "—")
        .replace("&#8211;", "–")
        .replace("&#8212;", "—")

    companion object {
        private const val PINS_LISTING_URL = "https://www.disneystore.com/collectibles/pins/"
        private const val BASE_URL = "https://www.disneystore.com"
        private const val IMAGE_CDN = "https://cdn-ssl.s7.shopdisney.com/is/image/DisneyShopping/"
        private const val TILE_MARKER = "class=\"product__tile \""
        private const val TEALIUM_ATTR = "data-tealium-productstring"

        private val TEALIUM_REGEX =
            Regex("""data-tealium-productstring="([^"]+)"""")
        private val IMAGE_SKU_REGEX =
            Regex("""DisneyShopping/(\d+)(?:-\d+)?\?fmt=jpeg""")
        private val PRODUCT_LINK_REGEX =
            Regex("""class="product__tile_full_link"[^>]*href="([^"]+)"""")
    }
}
