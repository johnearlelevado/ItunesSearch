package com.example.challenge.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.challenge.room.entities.ItunesResults
import io.reactivex.Flowable

@Dao
interface ItunesResultsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    suspend fun insertItunesResults(list: List<ItunesResults>)

    @Query("SELECT * FROM itunes_results ORDER BY id ASC")
    fun getItunesResults(): Flowable<List<ItunesResults>>


    @Query("DELETE FROM itunes_results")
    suspend fun deleteItunesResults()
}