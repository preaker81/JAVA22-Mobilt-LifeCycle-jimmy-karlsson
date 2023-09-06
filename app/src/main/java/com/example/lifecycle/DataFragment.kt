package com.example.lifecycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class DataFragment : Fragment() {

    lateinit var dataTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_data, container, false)
        dataTextView = view.findViewById(R.id.dataTextView)
        return view
    }

    fun setData(data: String) {
        dataTextView.text = data
    }
}
