package DAO

import Entidades.User
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface UserDAO {

    @Query("select * from usuarios")
    fun listaUser():List<User>

    @Insert
    fun insert(user: User)

    @Query("SELECT COUNT(*) FROM usuarios WHERE email = :email")
    suspend fun isEmailRegistered(email: String): Int

    @Query("SELECT COUNT(*) FROM usuarios WHERE email = :email AND password = :password")
    suspend fun isValidUser(email: String, password: String): Int


}
