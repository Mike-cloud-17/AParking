package com.example.aparking.authorization

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aparking.R
import com.example.aparking.SharedPreferencesHelper
import com.example.aparking.SuccessfulLoginActivity
import com.example.aparking.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegistrationActivity : AppCompatActivity() {
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val name = findViewById<EditText>(R.id.name)
        val surname = findViewById<EditText>(R.id.surname)
        val birthdate = findViewById<EditText>(R.id.birthdate)
        val carNumber = findViewById<EditText>(R.id.carNumber)
        val registerButton = findViewById<Button>(R.id.registerButton)

        sharedPreferencesHelper = SharedPreferencesHelper(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://209.38.249.233:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(APIService::class.java)

        registerButton.setOnClickListener {
            val enteredName = name.text.toString()
            val enteredSurname = surname.text.toString()
            val enteredBirthdate = birthdate.text.toString()
            val enteredCarNumber = carNumber.text.toString()
            val enteredPhoneNumber = sharedPreferencesHelper.getPhoneNumber()

            if (validateInput(enteredName, enteredSurname, enteredBirthdate, enteredCarNumber)) {
                val user = User(
                    fullName = "$enteredName $enteredSurname",
                    birthday = enteredBirthdate,
                    phone = enteredPhoneNumber ?: ""
                    // Добавьте номер автомобиля как поля в класс User
                )

                val userRegistration = UserRegistration(
                    name = "$enteredName $enteredSurname",
                    phoneNumber = enteredPhoneNumber ?: "" // Если телефон вводится где-то в другом месте, измените это поле соответственно
                )

                val call = service.registerUser(userRegistration)

                call.enqueue(object : Callback<RegisterResponse> {
                    override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                sharedPreferencesHelper.saveToken(it.token)
                                sharedPreferencesHelper.setIsUserLogged(true)

                                val intent = Intent(this@RegistrationActivity, SuccessfulLoginActivity::class.java)
                                startActivity(intent)
                            }
                        } else {
                            Toast.makeText(this@RegistrationActivity, "Registration error. Please try again.", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Toast.makeText(this@RegistrationActivity, "Connection error. Please try again.", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                Toast.makeText(this, "Invalid input.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInput(name:String, surname: String, birthDate: String, carNumber: String): Boolean {
        if (name.any { it.isDigit() } || surname.any { it.isDigit() }) {
            return false
        }

        if (birthDate.length != 10) {
            return false
        }

        if (carNumber.length != 8) {
            return false
        }

        return true
    }
}
