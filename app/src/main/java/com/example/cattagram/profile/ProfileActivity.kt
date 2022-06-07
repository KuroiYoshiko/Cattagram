package com.example.cattagram.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cattagram.R
import com.example.cattagram.mainpage.MainPageFragment

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.hide()
        supportFragmentManager.beginTransaction().add(R.id.profileContainer, ProfileFragment()).commit()
    }
}