package com.example.cattagram.splashscreen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.cattagram.R
import com.example.cattagram.login.LoginActivity
import com.example.cattagram.mainpage.MainPageActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        val sharedPref = this.getSharedPreferences("shared", Context.MODE_PRIVATE)
        val userId: Int = sharedPref.getInt("userId", -1)
        var startIntent: Intent? = null

        startIntent = if (userId == -1) {
            Intent(this, LoginActivity::class.java)
        } else {
            Intent(this, MainPageActivity::class.java)
        }

        Handler().postDelayed({
            startActivity(startIntent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        },3000)
    }

}