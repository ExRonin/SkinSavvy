package com.dicoding.asclepius.view.user

import android.content.Context
import android.content.Intent
import com.dicoding.skinSavvy.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNav(private val context: Context, private val bottomNavigationView: BottomNavigationView) {

    fun setupNavigation() {
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    context.startActivity(Intent(context, AboutUs::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
