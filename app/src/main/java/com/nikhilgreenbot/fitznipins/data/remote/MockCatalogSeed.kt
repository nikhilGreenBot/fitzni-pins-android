package com.nikhilgreenbot.fitznipins.data.remote

import com.nikhilgreenbot.fitznipins.data.remote.dto.OfficialProductDto
import java.time.Instant

/**
 * Curated seed catalog for v1.
 * Phase B: replace with real API call to your backend.
 * All product URLs point to the official shopDisney pins page.
 */
object MockCatalogSeed {

    private const val SHOP_DISNEY_PINS = "https://www.shopdisney.com/collectibles/pins/"

    val products: List<OfficialProductDto> = listOf(
        OfficialProductDto(
            id = "pin-001",
            title = "Mickey Mouse Icon Pin – Glitter",
            price = "$12.99",
            imageUrl = "https://cdn2.iconfinder.com/data/icons/mickey-mouse-flat/512/mickey_mouse_icon-01.png",
            productUrl = SHOP_DISNEY_PINS,
            franchise = "DISNEY",
            badges = listOf("NEW", "PIN_TASTIC_TUESDAY"),
            description = "Classic Mickey silhouette in glitter enamel. A must-have for any collector.",
            updatedAt = Instant.now().epochSecond
        ),
        OfficialProductDto(
            id = "pin-002",
            title = "WALL•E & EVE Romantic Scene Pin",
            price = "$14.99",
            imageUrl = "https://cdn2.iconfinder.com/data/icons/pixar-characters/512/wall_e.png",
            productUrl = SHOP_DISNEY_PINS,
            franchise = "PIXAR",
            badges = listOf("LIMITED_RELEASE"),
            description = "WALL•E holds up a sparkler as EVE floats nearby. Limited to 2,500.",
            updatedAt = Instant.now().epochSecond
        ),
        OfficialProductDto(
            id = "pin-003",
            title = "Spider-Man Miles Morales Sticker Pin Set",
            price = "$19.99",
            imageUrl = "https://cdn2.iconfinder.com/data/icons/marvel-superheroes/512/spider-man.png",
            productUrl = SHOP_DISNEY_PINS,
            franchise = "MARVEL",
            badges = listOf("NEW"),
            description = "Set of 3 enamel pins featuring Miles in iconic sticker art style.",
            updatedAt = Instant.now().epochSecond
        ),
        OfficialProductDto(
            id = "pin-004",
            title = "The Mandalorian & Grogu Boho Pin",
            price = "$16.99",
            imageUrl = "https://cdn2.iconfinder.com/data/icons/star-wars-filled/100/baby-yoda-512.png",
            productUrl = SHOP_DISNEY_PINS,
            franchise = "STAR_WARS",
            badges = listOf("NEW", "PIN_TASTIC_TUESDAY"),
            description = "Mando and The Child in a hand-illustrated boho art style.",
            updatedAt = Instant.now().epochSecond
        ),
        OfficialProductDto(
            id = "pin-005",
            title = "Cinderella Castle Spinner Pin",
            price = "$22.99",
            imageUrl = "https://cdn2.iconfinder.com/data/icons/magic-kingdom/512/castle.png",
            productUrl = SHOP_DISNEY_PINS,
            franchise = "DISNEY",
            badges = listOf("LIMITED_RELEASE", "PRE_ORDER"),
            description = "The castle spins to reveal a hidden Tinker Bell. Limited Edition 1,500.",
            updatedAt = Instant.now().epochSecond
        ),
        OfficialProductDto(
            id = "pin-006",
            title = "Up! — Carl & Ellie Young Love Pin",
            price = "$13.99",
            imageUrl = "https://cdn2.iconfinder.com/data/icons/pixar-characters/512/carl_fredricksen.png",
            productUrl = SHOP_DISNEY_PINS,
            franchise = "PIXAR",
            badges = listOf("SOLD_OUT"),
            description = "Young Carl and Ellie at their mailbox. This one makes everyone cry.",
            updatedAt = Instant.now().epochSecond
        ),
        OfficialProductDto(
            id = "pin-007",
            title = "Lilo & Stitch Surfboard Set",
            price = "$17.99",
            imageUrl = "https://cdn2.iconfinder.com/data/icons/disney-characters/512/stitch.png",
            productUrl = SHOP_DISNEY_PINS,
            franchise = "DISNEY",
            badges = listOf("NEW"),
            description = "Stitch rides a surfboard across the Hawaiian waves. Ohana forever.",
            updatedAt = Instant.now().epochSecond
        ),
        OfficialProductDto(
            id = "pin-008",
            title = "R2-D2 Holographic Pin",
            price = "$15.99",
            imageUrl = "https://cdn2.iconfinder.com/data/icons/star-wars-filled/100/r2d2-512.png",
            productUrl = SHOP_DISNEY_PINS,
            franchise = "STAR_WARS",
            badges = listOf("NEW", "LIMITED_RELEASE"),
            description = "R2 projects a hologram of Princess Leia. Lenticular effect on tilt.",
            updatedAt = Instant.now().epochSecond
        ),
    )
}
