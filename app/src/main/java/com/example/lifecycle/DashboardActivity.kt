package com.example.lifecycle

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class DashboardActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dataFragment: DataFragment

    // Regex patterns for validation
    private val namePattern = "^[a-zA-Z ]{3,}$".toRegex()
    private val emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Initialize SharedPreferences and DataFragment
        sharedPreferences = getSharedPreferences("MyApp", MODE_PRIVATE)
        dataFragment = DataFragment()

        // Setup UI elements
        setupToolbar()
        setupLogoutButton()
        setupYearSpinner()
        setupSaveButton()

        // Add DataFragment to UI
        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, dataFragment).commit()
    }

    override fun onResume() {
        super.onResume()
        val savedData = sharedPreferences.getString("userData", "")
        savedData?.let {
            if (it.isNotEmpty()) dataFragment.setData(it)
        }
    }

    // Setup the toolbar
    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    // Setup the logout button
    private fun setupLogoutButton() {
        val logoutButtonDashboard: Button = findViewById(R.id.logoutButtonDashboard)
        logoutButtonDashboard.setOnClickListener {
            sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
            navigateToMainActivity()
        }
    }

    // Setup the year spinner
    private fun setupYearSpinner() {
        val yearSpinner: Spinner = findViewById(R.id.yearSpinner)
        val years = Array(124) { i -> (1900 + i).toString() }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearSpinner.adapter = adapter
        yearSpinner.setSelection(81)
    }

    // Setup the save button and its click listener
    private fun setupSaveButton() {
        val saveButton: Button = findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
            handleSaveUserData()
        }
    }

    // Handles the saving of user data
    private fun handleSaveUserData() {
        val fullNameEditText = findViewById<EditText>(R.id.fullNameEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val fullName = fullNameEditText.text.toString()
        val email = emailEditText.text.toString()

        if (!isValidInput(fullName, namePattern)) {
            fullNameEditText.error = "Invalid name"
            return
        }

        if (!isValidInput(email, emailPattern)) {
            emailEditText.error = "Invalid email"
            return
        }

        val yearOfBirth = findViewById<Spinner>(R.id.yearSpinner).selectedItem.toString()
        val certified = findViewById<Switch>(R.id.certifiedSwitch).isChecked
        val genderId = findViewById<RadioGroup>(R.id.genderRadioGroup).checkedRadioButtonId
        val gender = findViewById<RadioButton>(genderId).text.toString()

        val dataString = buildDataString(fullName, email, yearOfBirth, certified, gender)
        saveDataToPreferences(dataString)
        dataFragment.setData(dataString)
    }

    // Build the data string
    private fun buildDataString(
        fullName: String,
        email: String,
        yearOfBirth: String,
        certified: Boolean,
        gender: String
    ): String {
        return "Full Name: $fullName\nEmail: $email\nYear of Birth: $yearOfBirth\nCertified: $certified\nGender: $gender"
    }

    // Save the data string to SharedPreferences
    private fun saveDataToPreferences(dataString: String) {
        sharedPreferences.edit().putString("userData", dataString).apply()
    }

    // Navigates to the MainActivity
    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }

    // Validates the given input string against the given pattern
    private fun isValidInput(input: String, pattern: Regex): Boolean {
        return pattern.matches(input)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_main -> {
                navigateToMainActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
