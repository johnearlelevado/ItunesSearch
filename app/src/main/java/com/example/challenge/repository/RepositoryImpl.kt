package com.example.challenge.repository

import androidx.lifecycle.MutableLiveData
import com.example.challenge.api.common.ApiResponse
import com.example.challenge.api.common.scheduler.SchedulerProvider
import com.example.challenge.api.itunes.dto.SearchResultModel
import com.example.challenge.api.itunes.service.ItunesService
import com.example.challenge.room.AppDatabase
import com.example.challenge.room.entities.ItunesResults
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class RepositoryImpl constructor(
    private val itunesService: ItunesService,
    private val db: AppDatabase,
    private val schedulerProvider: SchedulerProvider
): Repository {
    val itunesItemsResponseLiveData = MutableLiveData<ApiResponse<SearchResultModel>>()
    val compositeDisposable = CompositeDisposable()

    val itunesItemsResults = BehaviorSubject.create<List<ItunesResults>>()


    init {
        db.itunesDao().getItunesResults().subscribe {
            itunesItemsResults.onNext(it)
        }
    }

    /**
     * Fetch itunes items from the network
     **/
    override fun getItunesItems(searchTerm:String,entityType:String,country:String) {
        compositeDisposable.add(itunesService.getSearchItems(searchTerm,entityType,country)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .doOnSubscribe {itunesItemsResponseLiveData.value = ApiResponse.loading() }
            .subscribe({ response ->
                if (response.isSuccessful) {
                    val results = response.body()
                    val newResult = results?.resultModels?.map { r -> ItunesResults(
                        id = 0,
                        artistName = r.artistName,
                        artworkUrl100 = r.artworkUrl100,
                        longDescription = r.longDescription,
                        previewUrl = r.previewUrl,
                        trackName = r.trackName,
                        primaryGenreName = r.primaryGenreName,
                        trackPrice = r.trackPrice,
                        currency = r.currency
                    ) }?.toList()
                    GlobalScope.launch {
                        newResult?.let {
                            db.itunesDao().deleteItunesResults()
                            db.itunesDao().insertItunesResults(it)
                        }
                    }
                } else  {
                    try {
                        itunesItemsResponseLiveData.value = ApiResponse.error(response.code())
                    } catch (e: Exception) {
                        e.printStackTrace()
                        itunesItemsResponseLiveData.value = ApiResponse.fail(e)
                    }
                }
            }, { t ->
                t.printStackTrace()
                itunesItemsResponseLiveData.value = ApiResponse.fail(t)
            }))
    }

    override fun getCompositeDisposableObject(): CompositeDisposable {
        return compositeDisposable
    }

    override fun getItunesItemsObservable(): MutableLiveData<ApiResponse<SearchResultModel>> {
        return itunesItemsResponseLiveData
    }

    override fun getItunesItemsResults(): Flowable<List<ItunesResults>> {
        return itunesItemsResults.toFlowable(BackpressureStrategy.LATEST)
    }
}