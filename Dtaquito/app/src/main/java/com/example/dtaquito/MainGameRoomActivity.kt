package com.example.dtaquito

import Beans.GameRoom
import Beans.Player
import Interface.PlaceHolder
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class MainGameRoomActivity : AppCompatActivity() {

    lateinit var service: PlaceHolder
    lateinit var tokenManager: TokenManager
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: PlayerAdapter
    lateinit var roomNameTextView: TextView
    lateinit var districtTextView: TextView
    lateinit var dateTextView: TextView
    lateinit var timeTextView: TextView
    lateinit var formatTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_game_room)

        tokenManager = TokenManager(this)

        val retrofit = createRetrofit()
        service = retrofit.create(PlaceHolder::class.java)

        roomNameTextView = findViewById(R.id.roomName)
        districtTextView = findViewById(R.id.district)
        dateTextView = findViewById(R.id.date)
        timeTextView = findViewById(R.id.time)
        formatTextView = findViewById(R.id.format)


        recyclerView = findViewById(R.id.playerList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val gameRoomId = intent.getIntExtra("GAME_ROOM_ID", -1)
        if (gameRoomId != -1) {
            fetchGameRoomDetails(gameRoomId)
        } else {
            Toast.makeText(this, "Invalid game room ID", Toast.LENGTH_SHORT).show()
        }
    }



    private fun createRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(AuthInterceptor(tokenManager))
            .build()

        return Retrofit.Builder()
            .baseUrl("https://dtaquito-backend.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private fun fetchGameRoomDetails(gameRoomId: Int) {
        service.getRoomById(gameRoomId).enqueue(object : Callback<GameRoom> {
            override fun onResponse(call: Call<GameRoom>, response: Response<GameRoom>) {
                if (response.isSuccessful) {
                    val gameRooms = response.body()
                    if (gameRooms != null) {
                        roomNameTextView.text = gameRooms.roomName
                        districtTextView.text = "Distrito: ${gameRooms.sportSpace?.district}"
                        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
                        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
                        val date = inputFormat.parse(gameRooms.openingDate)

                        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

                        val formattedDate = dateFormat.format(date)
                        val formattedTime = timeFormat.format(date)

                        dateTextView.text = "Fecha: $formattedDate"
                        timeTextView.text = "Hora: $formattedTime"
                        formatTextView.text = "Formato: ${gameRooms.sportSpace?.gamemode}"

                        val creators = listOfNotNull(gameRooms.getCreatorAsPlayer())
                        adapter = PlayerAdapter(creators)
                        recyclerView.adapter = adapter

                        adapter = PlayerAdapter(creators)
                        recyclerView.adapter = adapter
                    } else {
                        Log.e("MainGameRoomActivity", "No game rooms found")
                        Toast.makeText(this@MainGameRoomActivity, "No game rooms found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorCode = response.code()
                    Log.e("MainGameRoomActivity", "Failed to fetch game rooms: HTTP $errorCode")
                    Toast.makeText(this@MainGameRoomActivity, "Failed to fetch game rooms: HTTP $errorCode", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<GameRoom>, t: Throwable) {
                t.printStackTrace()
                Log.e("MainGameRoomActivity", "Error: ${t.message}")
                Toast.makeText(this@MainGameRoomActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
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