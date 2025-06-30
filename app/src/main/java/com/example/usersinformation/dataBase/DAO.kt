package com.example.usersinformation.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    @Query("SELECT * FROM users")
    suspend fun getAll(): List<UserEntity>

    @Query("DELETE FROM users")
    suspend fun clearAll()
}

