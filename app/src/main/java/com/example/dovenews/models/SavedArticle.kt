package com.example.dovenews.models

import androidx.room.*
import java.sql.Timestamp

@Entity(
    foreignKeys = [ForeignKey(
        entity = Article::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("news_id")
    )], indices = [Index(value = arrayOf("news_id"), unique = true)], tableName = "saved"
)
class SavedArticle(newsId: Int) {
    @ColumnInfo(name = "news_id")
    val newsId: Int

    @PrimaryKey
    @ColumnInfo(name = "time_saved")
    var timestamp: Timestamp

    init {
        timestamp = Timestamp(System.currentTimeMillis())
        this.newsId = newsId
    }
}