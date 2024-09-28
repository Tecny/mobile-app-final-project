package DAO

import Entidades.Room
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RoomDAO {

    @Query ("SELECT * FROM room")
    fun listRoom(): List<Room>

    @Insert
    fun insert(room: Room)





}