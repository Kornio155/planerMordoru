package com.example.test

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val imageMap = mapOf(
        "Człowiek" to R.drawable.sword,
        "Elf" to R.drawable.bow,
        "Hobbit" to R.drawable.ring,
        "Czarodziej" to R.drawable.wand,
        "Krasnolud" to R.drawable.axe
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val spinner: Spinner = findViewById(R.id.rasa)
        val imageView: ImageView = findViewById(R.id.zdj)
        val rasy = arrayOf("Człowiek", "Elf", "Hobbit", "Czarodziej", "Krasnolud")

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, rasy
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position) as String
                val imageResId = imageMap[selectedItem]
                if (imageResId != null){
                    imageView.setImageResource(imageResId)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                imageView.setImageResource(0)
            }
        }
    }
}
