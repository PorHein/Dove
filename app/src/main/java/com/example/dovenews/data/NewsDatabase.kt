package com.example.dovenews.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dovenews.data.dao.HeadlinesDao
import com.example.dovenews.data.dao.SavedDao
import com.example.dovenews.data.dao.SourcesDao
import com.example.dovenews.models.Article
import com.example.dovenews.models.SavedArticle
import com.example.dovenews.models.Source

@Database(
    entities = [Article::class, Source::class, SavedArticle::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DatabaseConverters::class
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun headlinesDao(): HeadlinesDao?
    abstract fun sourcesDao(): SourcesDao?
    abstract fun savedDao(): SavedDao?

    companion object {
        private val LOCK = Any()
        private const val DATABASE_NAME = "news"
        private var sInstance: NewsDatabase? = null
        fun getInstance(context: Context): NewsDatabase? {
            if (sInstance == null) {
                synchronized(LOCK) {
                    sInstance = Room.databaseBuilder(
                        context.applicationContext,
                        NewsDatabase::class.java,
                        DATABASE_NAME
                    ).build()
                }
            }
            return sInstance
        }
    }
}