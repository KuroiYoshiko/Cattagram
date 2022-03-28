package com.example.cattagram.mainpage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.example.cattagram.R

class MainPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        supportFragmentManager.beginTransaction().add(R.id.mainPageContainer, MainPageFragment()).commit()

    }
}