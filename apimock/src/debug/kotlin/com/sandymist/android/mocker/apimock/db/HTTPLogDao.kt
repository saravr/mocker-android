package com.scribd.android.mocker.apimock.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HTTPLogDao {
    @Insert
    suspend fun insert(httpLogEntity: HTTPLogEntity)

    @Query("SELECT * FROM http_log_table")
    fun get(): Flow<List<HTTPLogEntity>>
}
