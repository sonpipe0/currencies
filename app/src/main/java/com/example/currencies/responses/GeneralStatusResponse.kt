package com.example.currencies.responses

import com.google.gson.annotations.SerializedName

open class GeneralStatusResponse (
    @SerializedName("time_last_update_unix")
    val timeLastUpdateUnix: Long = 0L,
    @SerializedName("time_last_update_utc")
    val timeLastUpdateUtc: String = "",
    @SerializedName("time_next_update_unix")
    val timeNextUpdateUnix: Long = 0L,
    @SerializedName("time_next_update_utc")
    val timeNextUpdateUtc: String = "",
): GeneralDocsResponse()

open class GeneralDocsResponse(
    val result: String = "",
    val documentation: String = "",
    @SerializedName("terms_of_use")
    val termsOfUse: String = "",
)