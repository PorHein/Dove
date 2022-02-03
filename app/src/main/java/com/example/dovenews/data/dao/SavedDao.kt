package com.example.dovenews.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dovenews.models.Article
import com.example.dovenews.models.SavedArticle

@Dao
interface SavedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(article: SavedArticle?)

    @Query("SELECT COUNT(news_id) > 0 FROM saved WHERE news_id = :articleId")
    fun isFavourite(articleId: Int): LiveData<Boolean?>?

    @Query("DELETE FROM saved WHERE news_id=:articleId")
    fun removeSaved(articleId: Int)

    @Query(
        "SELECT articles.* FROM articles, saved " +
                "WHERE articles.id == saved.news_id " +
                "ORDER BY saved.time_saved"
    )
    fun getallSaved(): LiveData<List<Article>>
}