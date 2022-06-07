package com.example.cattagram.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cattagram.R
import com.example.cattagram.login.LoginFragment

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()
        supportFragmentManager.beginTransaction().add(R.id.registerContainer, RegisterFragment()).commit()
    }
}