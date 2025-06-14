package com.example.currencies.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Data Access Object for favorite currencies
 */
@Dao
interface FavoriteCurrencyDao {
    /**
     * Insert a favorite currency
     * If the currency already exists, replace it
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favoriteCurrency: FavoriteCurrency)

    /**
     * Delete a favorite currency
     */
    @Delete
    suspend fun deleteFavorite(favoriteCurrency: FavoriteCurrency)

    /**
     * Get all favorite currencies
     * Returns a LiveData to observe changes
     */
    @Query("SELECT * FROM favorite_currencies ORDER BY code ASC")
    fun getAllFavorites(): LiveData<List<FavoriteCurrency>>

    /**
     * Check if a currency is a favorite
     */
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_currencies WHERE code = :currencyCode LIMIT 1)")
    suspend fun isFavorite(currencyCode: String): Boolean

    /**
     * Delete a favorite currency by code
     */
    @Query("DELETE FROM favorite_currencies WHERE code = :currencyCode")
    suspend fun deleteFavoriteByCode(currencyCode: String)

    /**
     * Update a favorite currency's values (exchange rate and change percentage)
     */
    @Query("UPDATE favorite_currencies SET value = :value, dailyChangePercentage = :dailyChangePercentage WHERE code = :currencyCode")
    suspend fun updateFavoriteCurrencyValues(currencyCode: String, value: Double, dailyChangePercentage: Double)
}
