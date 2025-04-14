package com.example.currencies.search_filter

enum class FilterType {
    CONTINENT,
    CURRENCY,
}

enum class CurrencyMode {
    MORE_THAN,
    LESS_THAN,
}


data class Filter(
    val type: FilterType,
    val value: String,
){
    init {
        if(type == FilterType.CONTINENT) {
            require(value in listOf("Africa", "Asia", "East Europe", "West Europe","America", "Oceania", "America")) {
                "Invalid continent value: $value"
            }
        } else if(type == FilterType.CURRENCY) {
            require(value.toIntOrNull() != null) {
                "Invalid currency value: $value"
            }
        }
    }
}



