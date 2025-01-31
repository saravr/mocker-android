package com.scribd.android.mocker.apimock.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface MockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mockEntity: MockEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(mockEntityList: List<MockEntity>)

    @Query("UPDATE mock_table SET enabled = :enabled WHERE id = :id")
    suspend fun setEnabled(id: Int, enabled: Boolean)

    @Query("DELETE FROM mock_table WHERE path = :path")
    suspend fun remove(path: String)

    @Query("SELECT COUNT(*) FROM mock_table WHERE path LIKE :path")
    fun contains(path: String): Int

    @Query("SELECT * FROM mock_table")
    fun getMockFlow(): Flow<List<MockEntity>>

    @Query("SELECT * FROM mock_table")
    fun getMocks(): List<MockEntity>

    @Query("DELETE FROM mock_table")
    fun deleteAll()

    @Transaction
    suspend fun replaceAll(mocks: List<MockEntity>) {
        deleteAll()
        insertAll(mocks)
    }
}
