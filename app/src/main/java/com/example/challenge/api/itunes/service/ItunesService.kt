package com.example.challenge.api.itunes.service

import com.example.challenge.api.itunes.dto.SearchResultModel
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesService {

    @GET("/search")
    fun getSearchItems(
        @Query("term") searchTerm: String,
        @Query("entity") entityType: String,
        @Query("country") country: String
    ): Observable<Response<SearchResultModel>>

}