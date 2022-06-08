package com.example.cattagram.picture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cattagram.R
import com.example.cattagram.login.LoginFragment

class AddNewPictureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_picture)
        supportActionBar?.hide()
        supportFragmentManager.beginTransaction().add(R.id.addPicContainer, AddNewPictureFragment()).commit()
    }
}