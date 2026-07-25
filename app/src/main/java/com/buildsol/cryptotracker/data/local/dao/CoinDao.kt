// app/src/main/java/com/buildsol/cryptotracker/data/local/dao/CoinDao.kt

package com.buildsol.cryptotracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.buildsol.cryptotracker.data.local.entity.CoinEntity
import kotlinx.coroutines.flow.Flow



@Dao
interface CoinDao {

    @Query("SELECT * FROM coins ORDER BY marketCapRank ASC")
    fun observeCoins(): Flow<List<CoinEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(coins: List<CoinEntity>)

    @Query("SELECT * FROM coins ORDER BY marketCapRank ASC LIMIT :limit OFFSET :offset")
    suspend fun getCoinsPage(limit: Int, offset: Int): List<CoinEntity>


    @Query("DELETE FROM coins")
    suspend fun clearAll()

    @Query("SELECT * FROM coins WHERE id = :id LIMIT 1")
    suspend fun getCoinById(id: String): CoinEntity?
}