package com.example.dtaquito

import Beans.SportSpace
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

class SportSpaceActivity : AppCompatActivity() {

    lateinit var service: PlaceHolder
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sport_space)

        tokenManager = TokenManager(this)

        val retrofit = createRetrofit()
        service = retrofit.create<PlaceHolder>(PlaceHolder::class.java)

        getAllSportSpaces()

        val createRoomBtn = findViewById<Button>(R.id.create_room_btn)
        createRoomBtn.setOnClickListener {
            val intent = Intent(this, CreateRoomActivity::class.java)
            startActivity(intent)
        }

        val gameRoomBtn = findViewById<Button>(R.id.game_rooms_btn)
        gameRoomBtn.setOnClickListener {
            val intent = Intent(this, GameRoomActivity::class.java)
            startActivity(intent)
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

    private fun getAllSportSpaces() {
        val token = tokenManager.getToken()
        if (token != null) {
            service.getAllSportSpaces().enqueue(object : Callback<List<SportSpace>> {
                override fun onResponse(
                    call: Call<List<SportSpace>>,
                    response: Response<List<SportSpace>>
                ) {
                    if (response.isSuccessful) {
                        val sportSpaces = response.body()
                        val sportSpacesList = mutableListOf<SportSpace>()

                        if (sportSpaces != null) {
                            for (item in sportSpaces) {
                                sportSpacesList.add(
                                    SportSpace(
                                        item.id, item.name, item.sportId, item.sportType,
                                        item.imageUrl, item.price, item.district,
                                        item.description, item.user, item.startTime,
                                        item.endTime, item.gamemode, item.amount
                                    )
                                )
                            }

                            val recycler = findViewById<RecyclerView>(R.id.recyclerView)
                            recycler.layoutManager = LinearLayoutManager(applicationContext)
                            recycler.adapter = SportSpaceAdapter(sportSpacesList)
                        } else {
                            println("Sport spaces not found")
                        }
                    } else {
                        println("Failed to get sport spaces with status code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<SportSpace>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        } else {
            println("Token not found")
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