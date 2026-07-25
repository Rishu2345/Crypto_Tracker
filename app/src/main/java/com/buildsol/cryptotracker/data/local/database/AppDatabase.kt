// app/src/main/java/com/buildsol/cryptotracker/data/local/database/AppDatabase.kt

package com.buildsol.cryptotracker.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.buildsol.cryptotracker.data.local.dao.CoinDao
import com.buildsol.cryptotracker.data.local.entity.CoinEntity

@Database(
    entities = [CoinEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun coinDao(): CoinDao
}