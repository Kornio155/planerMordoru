package com.example.test

import android.os.Build
import android.os.Bundle
import android.support.v4.os.IResultReceiver
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.log


class MainActivity : AppCompatActivity() {

    private val imageMap = mapOf(
        "Człowiek" to R.drawable.sword,
        "Elf" to R.drawable.bow,
        "Hobbit" to R.drawable.ring,
        "Czarodziej" to R.drawable.wand,
        "Krasnolud" to R.drawable.axe
    )

    var selectedDate: String = ""
    var selectedTime: String = ""
    var selectedRadio: String = ""
    var podroz: Int = 0


    @RequiresApi(Build.VERSION_CODES.O)
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
        //datapicker

        val dataSpinner: DatePicker = findViewById(R.id.date)
        dataSpinner.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            selectedDate = "$dayOfMonth/${monthOfYear+1}/$year"
            Log.d("MainActivity", "$selectedDate")
        }

        //timepicker

        val czas = findViewById<TimePicker>(R.id.time)
        czas.setIs24HourView(true)

        czas.setOnTimeChangedListener { view, hour, minute ->
        selectedTime = "$hour:$minute"
            Log.d("MainActivity", "$selectedTime")
        }

        //tu jest wypisanie kiedy i o ktorej podroz sie zaczyna
        val kiedy: TextView = findViewById(R.id.kiedy)
        dataSpinner.setOnDateChangedListener { _, year,  monthOfYear, dayOfMonth ->
            selectedDate = "$dayOfMonth/${monthOfYear+1}/$year"
            kiedy.text = "Wyruszasz $selectedDate o godzinie $selectedTime."
        }

        czas.setOnTimeChangedListener { _, hour, minute ->
            selectedTime = "$hour:$minute"
            kiedy.text = "Wyruszasz $selectedDate o godzinie $selectedTime."
        }

        //wyposazenie czyli obsluga switch
        val sciezki: Switch = findViewById(R.id.sciezki)
        sciezki.setOnClickListener {
            val czySciezki = sciezki.isChecked
            Log.d("cos", "$czySciezki")
        }


        //wyposazenie czyli checkboxy
        val plElfowcb: CheckBox = findViewById(R.id.plElfow)
        val lembasycb: CheckBox = findViewById(R.id.lembasy)
        val pochodniacb: CheckBox = findViewById(R.id.pochodnia)

        //priorytet
        val radioGrupa: RadioGroup = findViewById(R.id.priorytet)
        val wybraneId = radioGrupa.checkedRadioButtonId

        if(wybraneId != -1){
            val wybraneRadio: RadioButton = findViewById(wybraneId)
            selectedRadio = wybraneRadio.text.toString()
        }

        //seekbar
        val minuty: SeekBar = findViewById(R.id.czasMarszu)
        val wysminuty: TextView = findViewById(R.id.minuty)
        minuty.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progres: Int, fromUser: Boolean) {
                podroz = progres
                wysminuty.text = "Marsz będzie trwał $progres minut."
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

        //tu jest podsumowanie, nic dalej ma nie byc!!!!!!!

        val podsumowanie: TextView = findViewById(R.id.podsumowanie)
        val guzik: Button = findViewById(R.id.guziczek)
        val imie: EditText = findViewById(R.id.imie)

        guzik.setOnClickListener {
            val rasa = spinner.selectedItem.toString()
            val sciezki = if(sciezki.isChecked) "tak" else "nie"
            val wybraneId = radioGrupa.checkedRadioButtonId

            if (wybraneId == -1) {

                Toast.makeText(this, "Musisz wybrać intensywność marszu, zanim przejdziesz dalej!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val wyposazenie = mutableListOf<String>()
            if (plElfowcb.isChecked) wyposazenie.add("Płaszcz elfów")
            if (lembasycb.isChecked) wyposazenie.add("Lembasy")
            if (pochodniacb.isChecked) wyposazenie.add("Pochodnia")

            val podsumowanieText = """
                Imie: ${imie.text}
                Rasa: $rasa
                Wychodzisz $selectedDate o godzinie $selectedTime
                Czy chcesz dostęp do tajnych ścieżek? $sciezki
                Wyposażenie: ${if (wyposazenie.isEmpty()) "brak" else wyposazenie.joinToString(", ")}
                Marsz będzie: $selectedRadio
                Marsz będzie trwał: $podroz""".trimIndent()


            podsumowanie.text = podsumowanieText
        }
    }
}
