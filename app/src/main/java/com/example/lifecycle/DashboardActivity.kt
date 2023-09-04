package com.example.lifecycle

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class DashboardActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE)

        // Setup the Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val logoutButtonDashboard: Button = findViewById(R.id.logoutButtonDashboard)

        logoutButtonDashboard.setOnClickListener {
            sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val yearSpinner: Spinner = findViewById(R.id.yearSpinner)

// Create an ArrayAdapter using a simple spinner layout and years array
        val years = Array(124) { i -> (1900 + i).toString() }  // Change the range as you need
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

// Apply the adapter to the spinner
        yearSpinner.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("DashboardActivity", "Menu item clicked: ${item.itemId}")
        return when (item.itemId) {
            R.id.action_main -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
