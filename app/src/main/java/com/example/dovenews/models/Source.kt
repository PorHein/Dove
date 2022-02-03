package com.example.dovenews.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Source of news provided
 */
@Entity(tableName = "sources")
class Source(
    @field:ColumnInfo(name = "id") @field:PrimaryKey val id: String,
    @field:ColumnInfo(name = "name") val name: String,
    @field:ColumnInfo(name = "description") val description: String,
    @field:ColumnInfo(name = "url") val url: String,
    @field:ColumnInfo(name = "category") val category: String,
    @field:ColumnInfo(name = "language") val language: String,
    @field:ColumnInfo(name = "country") val country: String
)
