package com.example.cattagram.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cattagram.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportFragmentManager.beginTransaction().add(R.id.loginContainer, LoginFragment()).commit()
    }
}