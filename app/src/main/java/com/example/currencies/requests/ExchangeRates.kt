package com.example.currencies.requests

import com.example.currencies.CurrenciesApplication
import com.example.currencies.utils.csvData


val codeMap: Map<String, String> = mapOf(
        "AED" to "UAE Dirham",
        "AFN" to "Afghan Afghani",
        "ALL" to "Albanian Lek",
        "AMD" to "Armenian Dram",
        "ANG" to "Netherlands Antillian Guilder",
        "AOA" to "Angolan Kwanza",
        "ARS" to "Argentine Peso",
        "AUD" to "Australian Dollar",
        "AWG" to "Aruban Florin",
        "AZN" to "Azerbaijani Manat",
        "BAM" to "Bosnia and Herzegovina Convertible Mark",
        "BBD" to "Barbados Dollar",
        "BDT" to "Bangladeshi Taka",
        "BGN" to "Bulgarian Lev",
        "BHD" to "Bahraini Dinar",
        "BIF" to "Burundian Franc",
        "BMD" to "Bermudian Dollar",
        "BND" to "Brunei Dollar",
        "BOB" to "Bolivian Boliviano",
        "BRL" to "Brazilian Real",
        "BSD" to "Bahamian Dollar",
        "BTN" to "Bhutanese Ngultrum",
        "BWP" to "Botswana Pula",
        "BYN" to "Belarusian Ruble",
        "BZD" to "Belize Dollar",
        "CAD" to "Canadian Dollar",
        "CDF" to "Congolese Franc",
        "CHF" to "Swiss Franc",
        "CLP" to "Chilean Peso",
        "CNY" to "Chinese Renminbi",
        "COP" to "Colombian Peso",
        "CRC" to "Costa Rican Colon",
        "CUP" to "Cuban Peso",
        "CVE" to "Cape Verdean Escudo",
        "CZK" to "Czech Koruna",
        "DJF" to "Djiboutian Franc",
        "DKK" to "Danish Krone",
        "DOP" to "Dominican Peso",
        "DZD" to "Algerian Dinar",
        "EGP" to "Egyptian Pound",
        "ERN" to "Eritrean Nakfa",
        "ETB" to "Ethiopian Birr",
        "EUR" to "Euro",
        "FJD" to "Fiji Dollar",
        "FKP" to "Falkland Islands Pound",
        "FOK" to "Faroese Króna",
        "GBP" to "Pound Sterling",
        "GEL" to "Georgian Lari",
        "GGP" to "Guernsey Pound",
        "GHS" to "Ghanaian Cedi",
        "GIP" to "Gibraltar Pound",
        "GMD" to "Gambian Dalasi",
        "GNF" to "Guinean Franc",
        "GTQ" to "Guatemalan Quetzal",
        "GYD" to "Guyanese Dollar",
        "HKD" to "Hong Kong Dollar",
        "HNL" to "Honduran Lempira",
        "HRK" to "Croatian Kuna",
        "HTG" to "Haitian Gourde",
        "HUF" to "Hungarian Forint",
        "IDR" to "Indonesian Rupiah",
        "ILS" to "Israeli New Shekel",
        "IMP" to "Manx Pound",
        "INR" to "Indian Rupee",
        "IQD" to "Iraqi Dinar",
        "IRR" to "Iranian Rial",
        "ISK" to "Icelandic Króna",
        "JEP" to "Jersey Pound",
        "JMD" to "Jamaican Dollar",
        "JOD" to "Jordanian Dinar",
        "JPY" to "Japanese Yen",
        "KES" to "Kenyan Shilling",
        "KGS" to "Kyrgyzstani Som",
        "KHR" to "Cambodian Riel",
        "KID" to "Kiribati Dollar",
        "KMF" to "Comorian Franc",
        "KRW" to "South Korean Won",
        "KWD" to "Kuwaiti Dinar",
        "KYD" to "Cayman Islands Dollar",
        "KZT" to "Kazakhstani Tenge",
        "LAK" to "Lao Kip",
        "LBP" to "Lebanese Pound",
        "LKR" to "Sri Lanka Rupee",
        "LRD" to "Liberian Dollar",
        "LSL" to "Lesotho Loti",
        "LYD" to "Libyan Dinar",
        "MAD" to "Moroccan Dirham",
        "MDL" to "Moldovan Leu",
        "MGA" to "Malagasy Ariary",
        "MKD" to "Macedonian Denar",
        "MMK" to "Burmese Kyat",
        "MNT" to "Mongolian Tögrög",
        "MOP" to "Macanese Pataca",
        "MRU" to "Mauritanian Ouguiya",
        "MUR" to "Mauritian Rupee",
        "MVR" to "Maldivian Rufiyaa",
        "MWK" to "Malawian Kwacha",
        "MXN" to "Mexican Peso",
        "MYR" to "Malaysian Ringgit",
        "MZN" to "Mozambican Metical",
        "NAD" to "Namibian Dollar",
        "NGN" to "Nigerian Naira",
        "NIO" to "Nicaraguan Córdoba",
        "NOK" to "Norwegian Krone",
        "NPR" to "Nepalese Rupee",
        "NZD" to "New Zealand Dollar",
        "OMR" to "Omani Rial",
        "PAB" to "Panamanian Balboa",
        "PEN" to "Peruvian Sol",
        "PGK" to "Papua New Guinean Kina",
        "PHP" to "Philippine Peso",
        "PKR" to "Pakistani Rupee",
        "PLN" to "Polish Złoty",
        "PYG" to "Paraguayan Guaraní",
        "QAR" to "Qatari Riyal",
        "RON" to "Romanian Leu",
        "RSD" to "Serbian Dinar",
        "RUB" to "Russian Ruble",
        "RWF" to "Rwandan Franc",
        "SAR" to "Saudi Riyal",
        "SBD" to "Solomon Islands Dollar",
        "SCR" to "Seychellois Rupee",
        "SDG" to "Sudanese Pound",
        "SEK" to "Swedish Krona",
        "SGD" to "Singapore Dollar",
        "SHP" to "Saint Helena Pound",
        "SLE" to "Sierra Leonean Leone",
        "SLL" to "Sierra Leonean Leone",
        "SOS" to "Somali Shilling",
        "SRD" to "Surinamese Dollar",
        "SSP" to "South Sudanese Pound",
        "STN" to "São Tomé and Príncipe Dobra",
        "SYP" to "Syrian Pound",
        "SZL" to "Eswatini Lilangeni",
        "THB" to "Thai Baht",
        "TJS" to "Tajikistani Somoni",
        "TMT" to "Turkmenistan Manat",
        "TND" to "Tunisian Dinar",
        "TOP" to "Tongan Paʻanga",
        "TRY" to "Turkish Lira",
        "TTD" to "Trinidad and Tobago Dollar",
        "TVD" to "Tuvaluan Dollar",
        "TWD" to "New Taiwan Dollar",
        "TZS" to "Tanzanian Shilling",
        "UAH" to "Ukrainian Hryvnia",
        "UGX" to "Ugandan Shilling",
        "USD" to "United States Dollar",
        "UYU" to "Uruguayan Peso",
        "UZS" to "Uzbekistani So'm",
        "VES" to "Venezuelan Bolívar Soberano",
        "VND" to "Vietnamese Đồng",
        "VUV" to "Vanuatu Vatu",
        "WST" to "Samoan Tālā",
        "XAF" to "Central African CFA Franc",
        "XCD" to "East Caribbean Dollar",
        "XCG" to "Caribbean Guilder",
        "XDR" to "Special Drawing Rights",
        "XOF" to "West African CFA franc",
        "XPF" to "CFP Franc",
        "YER" to "Yemeni Rial",
        "ZAR" to "South African Rand",
        "ZMW" to "Zambian Kwacha",
        "ZWL" to "Zimbabwean Dollar"
    )
val currencyCodes: Map<String, Triple<String, String, Int>> = mockCurrencyCodes()


private fun mockCurrencyCodes(): Map<String, Triple<String, String, Int>> {
    val currencyCodes: MutableMap<String, Triple<String, String, Int>> = mutableMapOf()
    if (currencyCodes.isNotEmpty()) {
        return currencyCodes
    }
    val csvData: Map<String, Pair<String, String>> = csvData
    for (code in codeMap) {
        val codeKey = code.key
        if (csvData.containsKey(codeKey)) {
            val country = csvData[codeKey]?.first ?: continue
            val continent = csvData[codeKey]?.second ?: continue
            val codeImage = CurrenciesApplication.getResources().getIdentifier(
                country.lowercase(),
                "drawable",
                "com.example.currencies",
            )
            currencyCodes[codeKey] = Triple(continent, code.value, codeImage)
        }
    }

        return currencyCodes.entries
                .sortedBy { it.value.first }
                .associate { it.key to it.value }
}
