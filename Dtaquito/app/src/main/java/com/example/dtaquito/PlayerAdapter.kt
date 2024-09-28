package com.example.dtaquito

import Beans.Player
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PlayerAdapter(val players: List<Player>) : RecyclerView.Adapter<PlayerListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_player, parent, false)
        return PlayerListViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerListViewHolder, position: Int) {
        holder.renderPlayer(players[position])
    }

    override fun getItemCount(): Int = players.size
}