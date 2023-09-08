package com.example.lifecycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class DataFragment : Fragment() {

    // Declare the TextView that will display the data
    private lateinit var dataTextView: TextView

    // Inflate the layout and initialize dataTextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout
        val view = inflater.inflate(R.layout.fragment_data, container, false)

        // Initialize the TextView
        dataTextView = view.findViewById(R.id.dataTextView)

        return view
    }

    // Update the content of dataTextView
    fun setData(data: String) {
        dataTextView.text = data
    }
}
