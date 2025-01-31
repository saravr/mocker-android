package com.scribd.android.mocker.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep

class Users : ArrayList<Users.UsersItem>(){
    @Keep
    @Serializable
    data class UsersItem(
        @SerialName("address")
        val address: Address? = null,
        @SerialName("company")
        val company: Company? = null,
        @SerialName("email")
        val email: String?,
        @SerialName("id")
        val id: Int?,
        @SerialName("name")
        val name: String?,
        @SerialName("phone")
        val phone: String? = null,
        @SerialName("username")
        val username: String? = null,
        @SerialName("website")
        val website: String? = null,
    ) {
        @Keep
        @Serializable
        data class Address(
            @SerialName("city")
            val city: String?,
            @SerialName("geo")
            val geo: Geo? = null,
            @SerialName("street")
            val street: String? = null,
            @SerialName("suite")
            val suite: String? = null,
            @SerialName("zipcode")
            val zipcode: String? = null,
        ) {
            @Keep
            @Serializable
            data class Geo(
                @SerialName("lat")
                val lat: String?,
                @SerialName("lng")
                val lng: String?
            )
        }
    
        @Keep
        @Serializable
        data class Company(
            @SerialName("bs")
            val bs: String?,
            @SerialName("catchPhrase")
            val catchPhrase: String?,
            @SerialName("name")
            val name: String?
        )
    }
}