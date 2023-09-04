package com.example.lifecycle

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    var isFirstRun = true  // Add this flag to your class variables

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE)

        val logoutButtonMain: Button = findViewById(R.id.logoutButtonMain)

        if (sharedPreferences.getBoolean("isLoggedIn", false) && isFirstRun) {
            isFirstRun = false  // Set the flag to false

            val toolbar: Toolbar = findViewById(R.id.toolbar)
            setSupportActionBar(toolbar)
            logoutButtonMain.visibility = View.VISIBLE

            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        } else {
            logoutButtonMain.visibility = View.GONE
        }

        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (MockDb.checkLoggIn(username, password)) {
                sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
            } else {
                // Handle wrong credentials
            }
        }

        logoutButtonMain.setOnClickListener {
            sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        val logoutButtonMain: Button = findViewById(R.id.logoutButtonMain)

        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            val toolbar: Toolbar = findViewById(R.id.toolbar)
            setSupportActionBar(toolbar)
            logoutButtonMain.visibility = View.VISIBLE
        } else {
            logoutButtonMain.visibility = View.GONE
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            menuInflater.inflate(R.menu.menu_main, menu)
            return true
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("DashboardActivity", "Menu item clicked: ${item.itemId}")
        return when (item.itemId) {
            R.id.action_dashboard -> {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
