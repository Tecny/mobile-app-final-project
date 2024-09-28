package com.example.dtaquito


import Beans.Player
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class PlayerListViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val playerName: TextView = view.findViewById(R.id.playerName)

    fun renderPlayer(player: Player) {
        playerName.text = player.name
    }
}
