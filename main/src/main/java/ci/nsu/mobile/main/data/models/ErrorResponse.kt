package ci.nsu.mobile.main.data.models

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("message")
    val message: String? = null,

    @SerializedName("error")
    val error: String? = null,

    @SerializedName("statusCode")
    val statusCode: Int? = null,

    @SerializedName("timestamp")
    val timestamp: String? = null,

    @SerializedName("path")
    val path: String? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("detail")
    val detail: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("fieldErrors")
    val fieldErrors: Map<String, List<String>>? = null
)