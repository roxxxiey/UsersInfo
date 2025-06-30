package com.example.usersinformation.dataBase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.usersinformation.ApiUser
import com.google.gson.Gson

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val uuid: String,
    val jsonData: String
) {
    companion object {
        fun fromApiUser(user: ApiUser): UserEntity {
            return UserEntity(
                uuid = user.login.uuid,
                jsonData = Gson().toJson(user)
            )
        }
    }

    fun toApiUser(): ApiUser {
        return Gson().fromJson(jsonData, ApiUser::class.java)
    }
}