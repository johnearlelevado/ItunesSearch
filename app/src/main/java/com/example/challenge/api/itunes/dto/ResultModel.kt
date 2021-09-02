package com.example.challenge.api.itunes.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResultModel (
    @SerializedName("artistName")
    var artistName: String? = null,

    @SerializedName("trackName")
    var trackName: String? = null,

    @SerializedName("previewUrl")
    var previewUrl: String? = null,

    @SerializedName("artworkUrl100")
    var artworkUrl100: String? = null,

    @SerializedName("longDescription")
    var longDescription: String? = null,

    @SerializedName("trackPrice")
    var trackPrice: String? = null,

    @SerializedName("primaryGenreName")
    var primaryGenreName: String? = null,

    @SerializedName("currency")
    var currency: String? = null
)