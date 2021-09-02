package com.example.challenge.repository

import androidx.lifecycle.MutableLiveData
import com.example.challenge.api.common.ApiResponse
import com.example.challenge.api.itunes.dto.SearchResultModel
import com.example.challenge.room.entities.ItunesResults
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable

interface Repository {

    fun getCompositeDisposableObject(): CompositeDisposable

    fun getItunesItems(searchTerm:String,entityType:String,country:String)

    fun getItunesItemsObservable(): MutableLiveData<ApiResponse<SearchResultModel>>

    fun getItunesItemsResults(): Flowable<List<ItunesResults>>
}