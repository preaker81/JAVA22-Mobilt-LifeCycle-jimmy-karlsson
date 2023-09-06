package com.example.lifecycle

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class DashboardActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var dataFragment: DataFragment  // Define dataFragment at the class level

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
        val years = Array(124) { i -> (1900 + i).toString() }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearSpinner.adapter = adapter

        val saveButton: Button = findViewById(R.id.saveButton)

        // Initialize DataFragment
        dataFragment = DataFragment()

        saveButton.setOnClickListener {
            val fullName = findViewById<EditText>(R.id.fullNameEditText).text.toString()
            val email = findViewById<EditText>(R.id.emailEditText).text.toString()
            val yearOfBirth = findViewById<Spinner>(R.id.yearSpinner).selectedItem.toString()
            val certified = findViewById<Switch>(R.id.certifiedSwitch).isChecked
            val genderId = findViewById<RadioGroup>(R.id.genderRadioGroup).checkedRadioButtonId
            val gender = findViewById<RadioButton>(genderId).text.toString()

            val dataString = "Full Name: $fullName\nEmail: $email\nYear of Birth: $yearOfBirth\nCertified: $certified\nGender: $gender"
            sharedPreferences.edit().putString("userData", dataString).apply()

            dataFragment.setData(dataString)
        }

        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, dataFragment).commit()
    }

    override fun onResume() {
        super.onResume()
        val savedData = sharedPreferences.getString("userData", "")
        if (savedData != "") {
            dataFragment.setData(savedData!!)
        }
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
