package com.example.dtaquito

import Beans.Usuarios
import Interface.PlaceHolder
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class ProfileActivity : AppCompatActivity() {

    lateinit var nameInput : EditText
    lateinit var emailInput : EditText
    lateinit var passwordInput : EditText
    lateinit var updateBtn : Button
    lateinit var logoutBtn : Button
    lateinit var service: PlaceHolder
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        nameInput = findViewById(R.id.name_input)
        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        updateBtn = findViewById(R.id.update_btn)
        logoutBtn = findViewById(R.id.logout_btn)

        tokenManager = TokenManager(this)

        val retrofit = createRetrofit()
        service = retrofit.create(PlaceHolder::class.java)

        fillUserProfile()

        updateBtn.setOnClickListener{
            //updateUser()
        }

        logoutBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
    private fun createRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val urlLoggingInterceptor = Interceptor { chain ->
            val request: Request = chain.request()
            Log.d("URLInterceptor", "Request URL: ${request.url}")
            chain.proceed(request)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(urlLoggingInterceptor)
            .addInterceptor(AuthInterceptor(tokenManager))
            .build()

        return Retrofit.Builder()
            .baseUrl("https://dtaquito-backend.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
    private fun fillUserProfile() {
        val userId = tokenManager.getUserId()
        service.getUserId(userId).enqueue(object : Callback<Usuarios> {
            override fun onResponse(call: Call<Usuarios>, response: Response<Usuarios>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        nameInput.setText(user.name)
                        emailInput.setText(user.email)
                        passwordInput.setText("")
                    } else {
                        Toast.makeText(this@ProfileActivity, "User not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ProfileActivity, "Failed to fetch user data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Usuarios>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@ProfileActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /*private fun updateUser() {
        val userId = tokenManager.getUserId()
        val updatedUser = Usuarios(
            id = userId,
            name = nameInput.text.toString(),
            email = emailInput.text.toString(),
            password = passwordInput.text.toString(),

        )

        service.updateUser(userId, updatedUser).enqueue(object : Callback<Usuarios> {
            override fun onResponse(call: Call<Usuarios>, response: Response<Usuarios>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ProfileActivity, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ProfileActivity, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Usuarios>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@ProfileActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }*/
}