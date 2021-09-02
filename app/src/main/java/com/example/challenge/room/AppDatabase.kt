package com.example.challenge.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.challenge.room.dao.ItunesResultsDao
import com.example.challenge.room.entities.ItunesResults

@Database(entities = [ItunesResults::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itunesDao(): ItunesResultsDao
}