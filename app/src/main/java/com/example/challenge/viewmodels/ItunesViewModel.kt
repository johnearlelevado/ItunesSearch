package com.example.challenge.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import com.example.challenge.repository.Repository

open class ItunesViewModel @ViewModelInject constructor(
    val repository: Repository) : BaseViewModel() {

    val itunesItemsObservable
        get() = repository.getItunesItemsObservable()


    init {
        compositeDisposable = repository.getCompositeDisposableObject()
    }

    /**
     * Fetch itunes items
     **/
    fun getItunesItems(searchTerm:String,entityType:String,country:String) {
        repository.getItunesItems(searchTerm,entityType,country)
    }

}