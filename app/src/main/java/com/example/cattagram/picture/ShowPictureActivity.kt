package com.example.cattagram.picture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cattagram.R
import com.example.cattagram.mainpage.MainPageFragment

class ShowPictureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_picture)
        supportActionBar?.hide()
        supportFragmentManager.beginTransaction().add(R.id.showPicContainer, ShowPictureFragment()).commit()
    }
}