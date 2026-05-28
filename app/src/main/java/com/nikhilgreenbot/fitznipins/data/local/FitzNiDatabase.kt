package com.nikhilgreenbot.fitznipins.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nikhilgreenbot.fitznipins.data.local.dao.OfficialProductDao
import com.nikhilgreenbot.fitznipins.data.local.dao.UserPinDao
import com.nikhilgreenbot.fitznipins.data.local.entity.OfficialProductEntity
import com.nikhilgreenbot.fitznipins.data.local.entity.UserPinEntity

@Database(
    entities = [
        UserPinEntity::class,
        OfficialProductEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class FitzNiDatabase : RoomDatabase() {
    abstract fun userPinDao(): UserPinDao
    abstract fun officialProductDao(): OfficialProductDao

    companion object {
        const val DATABASE_NAME = "fitzni_pins.db"
    }
}
