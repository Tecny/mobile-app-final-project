package Beans

data class GameRoom(
    val id:Int,
    val creatorId:Int,
    val creator: Usuarios?,
    val sportSpace: SportSpace?,
    val openingDate: String,
    val day: String,
    val players: MutableList<Player> = mutableListOf(),
    val roomName: String
){
    fun getCreatorAsPlayer(): Player? {
        return creator?.let { Player(creatorId, it.name) }
    }
}