package com.scribd.android.mocker.service

import com.scribd.android.mocker.model.Account
import com.scribd.android.mocker.model.Users
import retrofit2.http.GET

interface NetworkService {
    @GET("/users.json")
    suspend fun getUsers(): List<Users.UsersItem>

    @GET("/account.json")
    suspend fun getAccount(): Account
}
