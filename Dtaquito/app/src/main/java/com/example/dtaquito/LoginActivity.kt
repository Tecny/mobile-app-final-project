package com.example.dtaquito

import DB.AppDataBase
import Beans.LoginRequest
import Beans.LoginResponse
import Interface.PlaceHolder
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class LoginActivity : AppCompatActivity() {

    private lateinit var appDB: AppDataBase
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginBtn: Button
    private lateinit var registerBtn: Button
    private lateinit var service: PlaceHolder
    private lateinit var tokenManager: TokenManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        tokenManager = TokenManager(this)
        val retrofit = createRetrofit()
        service = retrofit.create(PlaceHolder::class.java)

        appDB = AppDataBase.getDatabase(this)

        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        loginBtn = findViewById(R.id.login_btn)
        registerBtn = findViewById(R.id.register_btn)

        registerBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    "Por favor, ingresa tu email y contraseña.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            loginUser(email, password)
        }

        val savedToken = tokenManager.getToken()
        savedToken?.let {
            Log.d("LoginActivity", "Saved token: $it")
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

    private fun loginUser(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)
        service.loginUser(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    loginResponse?.let {
                        tokenManager.saveToken(it.token)
                        tokenManager.saveUserId(it.id)
                        verifyTokenAndRedirect()
                    }
                    Toast.makeText(
                        this@LoginActivity,
                        "Inicio de sesión exitoso.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Usuario o contraseña incorrectos.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("LoginActivity", "Failed to login with status code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginActivity", "Login failed", t)
            }
        })
    }

    private fun verifyTokenAndRedirect() {
        val savedToken = tokenManager.getToken()
        if (savedToken != null) {
            Log.d("LoginActivity", "Token saved successfully: $savedToken")
            redirectToHome()
        } else {
            Log.e("LoginActivity", "Token not found")
        }
    }

    private fun redirectToHome() {
        try {
            val intent = Intent(this, SportActivity::class.java)
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            Log.e("LoginActivity", "Error redirecting to SportActivity", e)
        }
    }
}