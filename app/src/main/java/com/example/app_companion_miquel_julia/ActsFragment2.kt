package com.example.app_companion_miquel_julia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class ActsFragment2 : Fragment() {

    private lateinit var nameText: TextView
    private lateinit var typeText: TextView
    private lateinit var isActiveText: TextView
    private lateinit var backButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_acts2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Inicializo Recycler
        initView(view)

        //Llamo a la api
        loadData()

        //Vuelvo a la pantalla que tenia guardada
        backButton.setOnClickListener{
            parentFragmentManager.popBackStack()
        }
    }

    private fun initView(view: View) {
        nameText = view.findViewById(R.id.actName)
        typeText = view.findViewById(R.id.actType)
        isActiveText = view.findViewById(R.id.actIsActive)
        backButton = view.findViewById<Button>(R.id.backButton)
    }

    private fun loadData() {
        val name = arguments?.getString("name")
        val type = arguments?.getString("type")
        val isActive = arguments?.getBoolean("isActive")

        nameText.text = name
        typeText.text = type
        isActiveText.text = if (isActive == true) "Active" else "Inactive"
    }
}