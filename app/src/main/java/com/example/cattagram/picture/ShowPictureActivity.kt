package com.example.cattagram.picture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.cattagram.R
import com.example.cattagram.mainpage.MainPageFragment

class ShowPictureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_picture)
        supportActionBar?.hide()

        val bundle = intent.extras
        var s: Int? = null
        s = bundle!!.getInt("image_id", 1)
        Log.d("Activity", "IMG passed id: $s")
        supportFragmentManager.beginTransaction().add(R.id.showPicContainer, ShowPictureFragment.newInstance(s, "ok")).commit()
    }
}