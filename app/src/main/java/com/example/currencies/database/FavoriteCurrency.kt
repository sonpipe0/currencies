package com.example.currencies.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.currencies.search.types.CurrencyRate

/**
 * Entity class for storing favorite currencies in Room database
 */
@Entity(tableName = "favorite_currencies")
data class FavoriteCurrency(
    @PrimaryKey
    val code: String,
    val name: String,
    val continent: String,
    val value: Double = 0.0,
    val dailyChangePercentage: Double = 0.0,
) {
    /**
     * Convert to CurrencyRate object
     * Using the stored value and dailyChangePercentage
     */
    fun toCurrencyRate(): CurrencyRate {
        return CurrencyRate(
            name = name,
            code = code,
            value = value,
            dailyChangePercentage = dailyChangePercentage,
            dailyChangeValue = 0.0, // We don't store this value, could be calculated if needed
            continent = continent
        )
    }

    companion object {
        /**
         * Create FavoriteCurrency from CurrencyRate
         * Capturing the current value and change percentage
         */
        fun fromCurrencyRate(currencyRate: CurrencyRate): FavoriteCurrency {
            return FavoriteCurrency(
                code = currencyRate.code,
                name = currencyRate.name,
                continent = currencyRate.continent,
                value = currencyRate.value,
                dailyChangePercentage = currencyRate.dailyChangePercentage
            )
        }
    }
}

