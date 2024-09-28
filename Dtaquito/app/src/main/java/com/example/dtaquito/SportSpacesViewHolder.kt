package com.example.dtaquito

import Beans.SportSpace
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class SportSpacesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val sportSpaceId: TextView = view.findViewById(R.id.idSport)
    private val sportSpaceName: TextView = view.findViewById(R.id.txtName)
    private val sportSpaceType: TextView = view.findViewById(R.id.txtSportType)
    private val sportSpacePrice: TextView = view.findViewById(R.id.txtPrice)
    private val sportSpaceDistrict: TextView = view.findViewById(R.id.txtDistrict)
    private val sportSpaceImage: ImageView = view.findViewById(R.id.imgSportSpace)
    private val sportSpaceDescription: TextView = view.findViewById(R.id.txtDescription)
    private val sportSpaceUser: TextView = view.findViewById(R.id.txtUser)
    private val sportSpaceEndTime: TextView = view.findViewById(R.id.txtEndTime)
    private val sportSpaceGameMode: TextView = view.findViewById(R.id.txtGameMode)
    private val sportSpaceAmount: TextView = view.findViewById(R.id.txtAmount)

    fun render(sportSpace: SportSpace) {
        sportSpaceId.text = sportSpace.id.toString()
        sportSpaceName.text = sportSpace.name
        sportSpaceType.text = sportSpace.sportType
        sportSpacePrice.text = sportSpace.price.toString()
        sportSpaceDistrict.text = sportSpace.district
        sportSpaceDescription.text = sportSpace.description
        sportSpaceUser.text = sportSpace.user?.name ?: "Unknown"
        sportSpaceEndTime.text = sportSpace.endTime
        sportSpaceGameMode.text = sportSpace.gamemode.toString()
        sportSpaceAmount.text = sportSpace.amount.toString()
        Picasso.get().load(sportSpace.imageUrl).into(sportSpaceImage)
    }
}