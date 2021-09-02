package com.example.challenge.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "itunes_results")
data class ItunesResults (
	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "id")
	var id: Int,

	@ColumnInfo(name = "artistName")
	var artistName: String? = null,

	@ColumnInfo(name = "trackName")
	var trackName: String? = null,

	@ColumnInfo(name = "previewUrl")
	var previewUrl: String? = null,

	@ColumnInfo(name = "artworkUrl100")
	var artworkUrl100: String? = null,

	@ColumnInfo(name = "longDescription")
	var longDescription: String? = null,

	@ColumnInfo(name = "trackPrice")
	var trackPrice: String? = null,

	@ColumnInfo(name = "primaryGenreName")
	var primaryGenreName: String? = null,

	@SerializedName("currency")
	var currency: String? = null
)