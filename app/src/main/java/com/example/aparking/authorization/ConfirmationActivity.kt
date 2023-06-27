package com.example.aparking.authorization

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aparking.R
import com.example.aparking.SharedPreferencesHelper
import com.example.aparking.SuccessfulLoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConfirmationActivity : AppCompatActivity() {
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        sharedPreferencesHelper = SharedPreferencesHelper(this)

        val code = findViewById<EditText>(R.id.code)
        val confirmButton = findViewById<Button>(R.id.confirm_button)

        val phoneNumber = intent.getStringExtra("PHONE_NUMBER")
        val requestId = intent.getStringExtra("REQUEST_ID") // полученный идентификатор запроса

        val retrofit = Retrofit.Builder()
            .baseUrl("http://209.38.249.233:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(APIService::class.java)

        confirmButton.setOnClickListener {
            val enteredCode = code.text.toString()
            val requestIdNonNull = requestId ?: return@setOnClickListener // добавлено обрабатывание null
            val phoneNumberNonNull = phoneNumber ?: return@setOnClickListener // добавлено обрабатывание null

            // Запрос на сервер для проверки кода подтверждения
            val call = service.authenticateUser(AuthenticateRequest(requestIdNonNull, enteredCode, phoneNumberNonNull))

            call.enqueue(object : Callback<AuthenticateResponse> {
                override fun onResponse(call: Call<AuthenticateResponse>, response: Response<AuthenticateResponse>) {
                    val responseBody = response.body() // добавлено обрабатывание null
                    if (response.isSuccessful && responseBody != null) {
                        // Сохраняем номер телефона
                        sharedPreferencesHelper.savePhoneNumber(phoneNumberNonNull)

                        if (responseBody.status == "login") {
                            // Сохраняем токен и переходим к главному экрану
                            val intent = Intent(this@ConfirmationActivity, SuccessfulLoginActivity::class.java)
                            startActivity(intent)
                        } else if (responseBody.status == "registration") {
                            // Переход к экрану регистрации
                            val intent = Intent(this@ConfirmationActivity, RegistrationActivity::class.java)
                            startActivity(intent)
                        }
                    } else {
                        // Обработка ошибки
                        val errorMessage = response.errorBody()?.string()
                        Log.d("ResponseError", "Ошибка при обработке запроса. Код ошибки: ${response.code()}")

                        Toast.makeText(this@ConfirmationActivity, "Не удалось проверить код. Попробуйте еще раз.", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<AuthenticateResponse>, t: Throwable) {
                    // Обработка ошибки
                    Toast.makeText(this@ConfirmationActivity, "Ошибка соединения с сервером. Попробуйте еще раз.", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}


