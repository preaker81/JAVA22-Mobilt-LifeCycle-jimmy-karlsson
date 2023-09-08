package com.example.lifecycle

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private var isFirstRun = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE)

        // Define UI elements
        val logoutButtonMain: Button = findViewById(R.id.logoutButtonMain)
        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)

        // Setup the initial UI state
        setupUI(logoutButtonMain, usernameEditText, passwordEditText, loginButton)

        // Set login button click listener
        loginButton.setOnClickListener {
            handleLogin(usernameEditText, passwordEditText)
        }

        // Set logout button click listener
        logoutButtonMain.setOnClickListener {
            handleLogout()
        }
    }

    // Handles UI setup based on login state
    private fun setupUI(logoutButtonMain: Button, usernameEditText: EditText, passwordEditText: EditText, loginButton: Button) {
        if (sharedPreferences.getBoolean("isLoggedIn", false) && isFirstRun) {
            isFirstRun = false
            setupToolbar()
            toggleUIState(logoutButtonMain, usernameEditText, passwordEditText, loginButton, false)
            navigateToDashboard()
        } else {
            toggleUIState(logoutButtonMain, usernameEditText, passwordEditText, loginButton, true)
        }
    }

    // Handles login logic
    private fun handleLogin(usernameEditText: EditText, passwordEditText: EditText) {
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (MockDb.checkLoggIn(username, password)) {
            sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
            navigateToDashboard()
        } else {
            Toast.makeText(this, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show()
        }
    }

    // Handles logout logic
    private fun handleLogout() {
        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
        finish()
        startActivity(intent)
    }

    // Sets up the toolbar
    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    // Navigates to the Dashboard Activity
    private fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    // Toggles the UI state based on login
    private fun toggleUIState(logoutButtonMain: Button, usernameEditText: EditText, passwordEditText: EditText, loginButton: Button, isEnabled: Boolean) {
        logoutButtonMain.visibility = if (isEnabled) View.GONE else View.VISIBLE
        usernameEditText.isEnabled = isEnabled
        passwordEditText.isEnabled = isEnabled
        loginButton.isEnabled = isEnabled
    }

    override fun onResume() {
        super.onResume()
        val logoutButtonMain: Button = findViewById(R.id.logoutButtonMain)
        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        toggleUIState(logoutButtonMain, usernameEditText, passwordEditText, loginButton, !isLoggedIn)

        if (isLoggedIn) {
            setupToolbar()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            menuInflater.inflate(R.menu.menu_main, menu)
            return true
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_dashboard -> {
                navigateToDashboard()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
