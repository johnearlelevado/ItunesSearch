package com.example.challenge.api.itunes.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SearchResultModel (
    @SerializedName("resultCount")
    @Expose
    var resultCount: Int = 0,

    @SerializedName("results")
    @Expose
    var resultModels: List<ResultModel>? = null
)