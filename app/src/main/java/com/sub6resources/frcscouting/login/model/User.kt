package com.sub6resources.frcscouting.login.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import java.util.*

/**
 * Created by whitaker on 1/9/18.
 */
@Entity()
class User() {
    @PrimaryKey
    lateinit var username: String
    lateinit var token: String

    @Ignore
    constructor(_username: String, _token: String) : this() {
        this.username = _username
        this.token = _token
    }
}

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM User WHERE username = :arg0")
    fun signIn(username: String): LiveData<User>

    @Query("SELECT * FROM User")
    fun getUsers(): LiveData<List<User>>
}