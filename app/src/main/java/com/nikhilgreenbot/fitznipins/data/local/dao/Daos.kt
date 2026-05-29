package com.nikhilgreenbot.fitznipins.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.nikhilgreenbot.fitznipins.data.local.entity.OfficialProductEntity
import com.nikhilgreenbot.fitznipins.data.local.entity.UserPinEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPinDao {

    @Upsert
    suspend fun upsert(pin: UserPinEntity)

    @Query("SELECT * FROM user_pins WHERE is_wishlist = 0 ORDER BY updated_at DESC")
    fun observeCollection(): Flow<List<UserPinEntity>>

    @Query("SELECT * FROM user_pins WHERE is_wishlist = 1 ORDER BY updated_at DESC")
    fun observeWishlist(): Flow<List<UserPinEntity>>

    @Query("SELECT * FROM user_pins WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): UserPinEntity?

    @Query("DELETE FROM user_pins WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("""
        SELECT * FROM user_pins
        WHERE title     LIKE '%' || :query || '%'
           OR description LIKE '%' || :query || '%'
           OR tags       LIKE '%' || :query || '%'
        ORDER BY updated_at DESC
    """)
    fun search(query: String): Flow<List<UserPinEntity>>

    @Query("SELECT COUNT(*) FROM user_pins WHERE is_wishlist = 0")
    fun countCollection(): Flow<Int>
}

@Dao
interface OfficialProductDao {

    @Upsert
    suspend fun upsertAll(products: List<OfficialProductEntity>)

    @Upsert
    suspend fun upsert(product: OfficialProductEntity)

    @Query("SELECT * FROM official_products ORDER BY updated_at DESC")
    fun observeAll(): Flow<List<OfficialProductEntity>>

    @Query("SELECT * FROM official_products WHERE franchise = :franchise ORDER BY updated_at DESC")
    fun observeByFranchise(franchise: String): Flow<List<OfficialProductEntity>>

    @Query("SELECT * FROM official_products WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): OfficialProductEntity?

    @Query("UPDATE official_products SET is_in_wishlist = NOT is_in_wishlist WHERE id = :id")
    suspend fun toggleWishlist(id: String)

    @Query("SELECT COUNT(*) FROM official_products")
    suspend fun count(): Int

    @Query("DELETE FROM official_products")
    suspend fun deleteAll()
}
