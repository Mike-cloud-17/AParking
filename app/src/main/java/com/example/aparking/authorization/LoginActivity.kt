package com.example.aparking.authorization

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aparking.MapActivity
import com.example.aparking.R
import com.example.aparking.SharedPreferencesHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferencesHelper = SharedPreferencesHelper(this)

        if (sharedPreferencesHelper.isUserLogged()) {
            // Переход на экран с картой, так как пользователь уже вошел
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        } else {
            // Если пользователь еще не вошел, то отображаем экран входа
            val phoneNumber = findViewById<EditText>(R.id.phoneNumber)
            val loginButton = findViewById<Button>(R.id.loginButton)

            val retrofit = Retrofit.Builder()
                .baseUrl("http://209.38.249.233:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(APIService::class.java)

            loginButton.setOnClickListener {
                val enteredPhoneNumber = phoneNumber.text.toString()

                val call = service.sendAuthRequest(AuthRequest(enteredPhoneNumber))

                call.enqueue(object : Callback<AuthResponse> {
                    override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                        if (response.isSuccessful) {
                            // Сохраняем номер телефона и requestId
                            sharedPreferencesHelper.savePhoneNumber(enteredPhoneNumber)
                            response.body()?.requestId?.let {
                                sharedPreferencesHelper.saveRequestId(it)
                            }

                            val intent = Intent(this@LoginActivity, ConfirmationActivity::class.java)
                            intent.putExtra("PHONE_NUMBER", enteredPhoneNumber)
                            intent.putExtra("REQUEST_ID", sharedPreferencesHelper.getRequestId()) // передаем requestId
                            startActivity(intent)
                        } else {
                            // Обработка ошибки
                            Log.d("ResponseError", response.errorBody()?.string().toString())
                            Toast.makeText(this@LoginActivity, "Не удалось отправить код. Попробуйте еще раз.", Toast.LENGTH_LONG).show()
                        }
                    }


                    override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                        // Выводим ошибку в логи
                        Log.e("ServerError", "Ошибка: ${t.message}")

                        // Обработка ошибки
                        Toast.makeText(this@LoginActivity, "Ошибка соединения с сервером. Попробуйте еще раз.", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }
}



