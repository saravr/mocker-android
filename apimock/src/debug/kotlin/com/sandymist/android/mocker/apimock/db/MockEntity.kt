package com.scribd.android.mocker.apimock.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mock_table")
data class MockEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val method: String,
    val path: String,
    val payload: String,
    val enabled: Boolean,
    val partial: Boolean,
    val createdAt: Long = System.currentTimeMillis(),
)
