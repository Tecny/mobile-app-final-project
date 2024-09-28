package com.example.dtaquito

import Beans.GameRoom
import Interface.PlaceHolder
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
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

class GameRoomActivity : AppCompatActivity() {

    lateinit var service: PlaceHolder
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_soccer_room)

        tokenManager = TokenManager(this)


        val retrofit = createRetrofit()
        service = retrofit.create<PlaceHolder>(PlaceHolder::class.java)

        getAllRooms()

        val createRoomBtn = findViewById<Button>(R.id.create_room_btn)
        createRoomBtn.setOnClickListener {
            val intent = Intent(this, CreateRoomActivity::class.java)
            startActivity(intent)
        }

        val goToSportSpaceCardsBtn = findViewById<Button>(R.id.sportbtn)
        goToSportSpaceCardsBtn.setOnClickListener {
            goToSportSpaceCards(it)
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

    private fun getAllRooms() {
        val token = tokenManager.getToken()
        if (token != null) {
            service.getAllRooms().enqueue(object : Callback<List<GameRoom>> {
                override fun onResponse(
                    call: Call<List<GameRoom>>,
                    response: Response<List<GameRoom>>
                ) {
                    if (response.isSuccessful) {
                        val gameRooms = response.body()
                        val gameRoomsList = mutableListOf<GameRoom>()

                        if (gameRooms != null) {
                            gameRoomsList.addAll(gameRooms)

                            val recycler = findViewById<RecyclerView>(R.id.recyclerView)
                            recycler.layoutManager = LinearLayoutManager(applicationContext)
                            recycler.adapter = GameRoomAdapter(gameRoomsList) // Attach the adapter here
                        } else {
                            println("Game rooms not found")
                        }
                    } else {
                        println("Failed to get game rooms with status code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<GameRoom>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        } else {
            println("Token not found")
        }
    }

    fun goToSportSpaceCards(view: View) {
        val intent = Intent(this, SportSpaceActivity::class.java)
        startActivity(intent)
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