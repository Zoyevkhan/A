package com.tv9news.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tv9news.models.masterHit.Language

@Database(
    entities = [Language::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class LocalRoomDatabase : RoomDatabase() {
    abstract fun languageDao(): LanguageDao
    companion object {
        @Volatile
        private var INSTANCE: LocalRoomDatabase? = null
        fun getDatabase(context: Context): LocalRoomDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = buildDatabase(context)
                }
            }
            return INSTANCE!!
        }
        private fun buildDatabase(context: Context): LocalRoomDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                LocalRoomDatabase::class.java,
                "tv9_database"
            ).build()
        }
    }
}