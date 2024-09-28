package com.example.dtaquito

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sport)
    }
    fun soccer(view: View) {
        val texto = view.contentDescription.toString()
        val soccerString = getString(R.string.soccer)
        if (texto == soccerString) {
            val intent = Intent(this, GameRoomActivity::class.java)
            startActivity(intent)
        }
    }

    fun pool(view: View) {
        val texto = view.contentDescription.toString()
        val poolString = getString(R.string.pool)
        if (texto == poolString) {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
    fun profile(view: View) {
        val texto = view.contentDescription.toString()
        val profileString = getString(R.string.perfil)
        if (texto == profileString) {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}