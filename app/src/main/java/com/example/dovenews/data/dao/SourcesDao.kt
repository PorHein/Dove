package com.example.dovenews.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dovenews.models.Source

@Dao
interface SourcesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun bulkInsert(sources: List<Source?>?)

    @Query("SELECT * FROM sources")
    fun getAllSources(): LiveData<List<Source>>
}