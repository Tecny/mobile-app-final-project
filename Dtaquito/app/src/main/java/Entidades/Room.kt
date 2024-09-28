package Entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "room")
data class Room (

    @PrimaryKey (autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "sport")
    val sport:String,
    @ColumnInfo(name = "location")
    val location:String,
    @ColumnInfo(name = "date")
    val date:String,
    @ColumnInfo(name = "time")
    val time:String,
    @ColumnInfo(name = "format")
    val format:String,
    @ColumnInfo(name = "room name")
    val roomName: String,

)