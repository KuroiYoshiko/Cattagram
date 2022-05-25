package com.example.cattagram.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cattagram.R
import com.example.cattagram.mainpage.MainPageFragment

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        supportFragmentManager.beginTransaction().add(R.id.searchContainer, SearchFragment()).commit()
    }
}