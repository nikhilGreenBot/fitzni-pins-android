package com.nikhilgreenbot.fitznipins

import com.nikhilgreenbot.fitznipins.data.remote.DisneyStoreCatalogFetcher
import okhttp3.OkHttpClient
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DisneyStoreCatalogFetcherTest {

  private val fetcher = DisneyStoreCatalogFetcher(OkHttpClient())

  @Test
  fun `parseProducts extracts id title price and image from tile html`() {
    val html = """
      class="product__tile "
      data-tealium-productstring="{&quot;id&quot;:&quot;438010247472&quot;,&quot;name&quot;:&quot;Grogu Pin &ndash; Star Wars&quot;,&quot;price&quot;:&quot;21.99&quot;,&quot;message&quot;:&quot;new&quot;,&quot;availability&quot;:&quot;online - in_stock&quot;,&quot;pims_character_name&quot;:&quot;Grogu&quot;}"
      >
      <a class="product__tile_full_link" href="/grogu-pin-438010247472.html">
      <source srcset="https://cdn-ssl.s7.shopdisney.com/is/image/DisneyShopping/3801059591046?fmt=jpeg&amp;qlt=90&amp;wid=493&amp;hei=493">
    """.trimIndent()

    val products = fetcher.parseProducts(html)

    assertTrue(products.size == 1)
    val product = products.first()
    assertTrue(product.id == "438010247472")
    assertTrue(product.title.contains("Grogu"))
    assertTrue(product.price == "${'$'}21.99")
    assertTrue(product.imageUrl.contains("3801059591046"))
    assertTrue(product.productUrl.contains("disneystore.com"))
  }
}
