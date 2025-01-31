package com.scribd.android.mocker.apimock.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [HTTPLogEntity::class, MockEntity::class], version = 1, exportSchema = false)
@TypeConverters(HTTPLogTypeConverters::class)
abstract class HTTPLogDatabase : RoomDatabase() {
    abstract fun httpLogDao(): HTTPLogDao
    abstract fun mockDao(): MockDao

    companion object {
        @Volatile
        private var instance: HTTPLogDatabase? = null
        private const val DB_FILE_NAME = "httplog.db"

        fun getDatabase(context: Context): HTTPLogDatabase {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    HTTPLogDatabase::class.java,
                    DB_FILE_NAME
                ).build()
                instance = newInstance
                newInstance
            }
        }
    }
}
