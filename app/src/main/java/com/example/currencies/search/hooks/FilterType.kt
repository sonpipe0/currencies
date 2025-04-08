package com.example.currencies.search.hooks

import java.util.Optional


enum class FilterType {
    CONTINENT,
    CURRENCY,
}

data class FilterData(
    private val type: FilterType,
    private val value: String,
    val enabled: Boolean = false,
) {
    init {
        when (type) {
            FilterType.CURRENCY -> {
                if (value.toDoubleOrNull() == null) {
                    throw IllegalArgumentException("Currency value must be a number")
                }
            }

            FilterType.CONTINENT -> TODO()
        }
    }


    fun getType(): FilterType {
        return type
    }

    fun getContinent(): Optional<String> {
        return when (type) {
            FilterType.CURRENCY -> Optional.empty()
            FilterType.CONTINENT -> Optional.of(value)
        }
    }

    fun getCurrency(): Optional<Double> {
        return when (type) {
            FilterType.CURRENCY -> Optional.of(value.toDouble())
            FilterType.CONTINENT -> Optional.empty()
        }
    }

    fun isEnabled(): Boolean {
        return enabled
    }

    fun setEnabled(enabled: Boolean): FilterData {
        return FilterData(type, value, enabled)
    }
}