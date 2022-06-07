package com.example.cattagram.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cattagram.R

class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        supportActionBar?.hide()
        supportFragmentManager.beginTransaction().add(R.id.editContainer, EditProfileFragment()).commit()
    }
}