package com.scribd.android.mocker.repository

import com.scribd.android.mocker.model.Account
import com.scribd.android.mocker.model.Users
import com.scribd.android.mocker.service.NetworkService
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestRepository @Inject constructor(
    private val networkService: NetworkService,
) {
    suspend fun getUsers(): List<Users.UsersItem> = try{
        networkService.getUsers()
    } catch (exception: Exception) {
        Timber.e("Failed to parse getUsers response")
        emptyList()
    }

    suspend fun getAccount(): Account = networkService.getAccount()
}
