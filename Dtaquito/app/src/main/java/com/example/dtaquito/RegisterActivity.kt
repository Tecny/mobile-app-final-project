package com.example.dtaquito

import DB.AppDataBase
import Beans.Usuarios
import Entidades.User
import Interface.PlaceHolder
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var appDB: AppDataBase
    private var isRoleSelected = false
    private var selectedRolePosition = 0
    private lateinit var selectedRole: String
    private lateinit var service: PlaceHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        appDB = AppDataBase.getDatabase(this)

        val retrofit = createRetrofit()
        service = retrofit.create(PlaceHolder::class.java)

        val spinner = findViewById<Spinner>(R.id.rol_input)
        val items = listOf("Rol", "Jugador", "Propietario")

        val adapter = ArrayAdapter(this, R.layout.spinner_items, items)
        adapter.setDropDownViewResource(R.layout.spinner_items)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                selectedRolePosition = position
                isRoleSelected = position != 0
                selectedRole = when (position) {
                    1 -> "R" // Jugador
                    2 -> "P" // Propietario
                    else -> ""
                }
                Toast.makeText(this@RegisterActivity, items[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                isRoleSelected = false
            }
        }

        val nameInput = findViewById<EditText>(R.id.name_input)
        val emailInput = findViewById<EditText>(R.id.email_input)
        val passwordInput = findViewById<EditText>(R.id.password_input)
        val paypalEmailInput = findViewById<EditText>(R.id.paypal_input)
        val registerBtn = findViewById<Button>(R.id.register_btn)

        registerBtn.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val paypal = paypalEmailInput.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa tu nombre.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Por favor, ingresa un email válido.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!isRoleSelected) {
                Toast.makeText(this, "Por favor, selecciona un rol.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedRolePosition == 0) {
                Toast.makeText(this, "Rol no válido. Por favor, selecciona un rol.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(paypal).matches()) {
                Toast.makeText(this, "Por favor, ingresa un email de PayPal válido.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val apiUser = Usuarios(
                id=null,
                name,
                email,
                password,
                listOf(selectedRole),
                paypal
            )

            service.createUser(apiUser).enqueue(object : Callback<Usuarios> {
                override fun onResponse(call: Call<Usuarios>, response: Response<Usuarios>) {
                    if (response.isSuccessful) {
                        //saveUserToDatabase(apiUser)
                        Toast.makeText(this@RegisterActivity, "Usuario registrado correctamente.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@RegisterActivity, "Error al registrar usuario.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Usuarios>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, "Error de red.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun createRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val urlLoggingInterceptor = Interceptor { chain ->
            val request: Request = chain.request()
            Log.d("URLInterceptor", "Request URL: ${request.url}")
            chain.proceed(request)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(urlLoggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://dtaquito-backend.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

//    private fun saveUserToDatabase(usuarios: Usuarios) {
//        val user = User(
//            id = 0, // Auto-generated by Room
//            name = usuarios.name,
//            email = usuarios.email,
//            password = usuarios.password,
//            roles = usuarios.roles.joinToString(","),
//            bankAccount = usuarios.bankAccount
//        )
//
//        GlobalScope.launch(Dispatchers.IO) {
//            appDB.userDao().insert(user)
//        }
//    }
}