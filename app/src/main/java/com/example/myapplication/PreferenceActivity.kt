package com.example.myapplication

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// In PreferenceActivity.kt

class PreferenceActivity : AppCompatActivity(), DistanceFragment.OnDistanceSelectedListener,
    AgeFragment.OnAgeSelectedListener {
    private lateinit var backButton: Button
    private lateinit var tvDistance: TextView
    private lateinit var tvAge: TextView
    private var selectedDistance: Int = 0 // Variable to hold the selected distance
    private lateinit var tvReligion: TextView
    private lateinit var tvEthnicity: TextView
    private lateinit var tvInterest: TextView
    private lateinit var tvDrink: TextView
    private lateinit var tvDrugs: TextView
    private lateinit var tvSmoking: TextView
    private lateinit var tvPolitics: TextView



    private val religion = arrayOf(
        "Agnostic", "Atheist", "Buddhist", "Catholic", "Christian", "Hindu", "Jewish", "Muslim",
        "Sikh", "Spiritual", "Other", "Open to all")
    private val ethnicity = arrayOf(
        "Black/African Descent", "East Asian", "Hispanic/Latino", "Middle Eastern", "Native American",
        "Pacific Islander", "South Asian", "Southeast Asian", "White/Caucasian", "Other", "Open to all")
    private val interested = arrayOf(
        "Men", "Women", "Nonbinary", "Everyone"
    )
    private val drinking = arrayOf(
        "Yes", "No"
    )
    private val drugs = arrayOf(
        "Yes", "No"
    )
    private val smoking = arrayOf(
        "Yes", "No"
    )
    private val politics = arrayOf(
        "Liberal", "Moderate", "Conservative", "Not Political", "Other", "Open to anything"
    )
    private var selectedReligion: String? = null
    private var selectedEthnicity: String? = null
    private var selectedInterested: String? = null
    private var selectedMaxAge: Int? = null
    private var selectedMinAge: Int? = null
    private var selectedDrink: String? = null
    private var selectedDrugs: String? = null
    private var selectedSmoking: String? = null
    private var selectedPolitics: String? = null





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preference)
        backButton = findViewById(R.id.next_button)
        tvInterest = findViewById(R.id.tvInterested)
        tvInterest.setOnClickListener {
            showInterestedPicker()
        }
        // Initialize the TextView
        tvDistance = findViewById(R.id.tvDistance)
        tvDistance.setOnClickListener {
            val fragment = DistanceFragment().also {
                it.setDistanceListener(this)
                backButton.visibility = View.GONE

            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container1, fragment)
                .addToBackStack(null)
                .commit()
        }
        tvReligion = findViewById(R.id.tvReligion)
        tvReligion.setOnClickListener {
            showReligionPicker()
        }
        tvEthnicity = findViewById(R.id.tvEthnicity)
        tvEthnicity.setOnClickListener {
            showEthnicityPicker()
        }
        tvAge = findViewById(R.id.tvAge)
        tvAge.setOnClickListener {
            val fragment = AgeFragment().also {
                it.setAgeListener(this)
                backButton.visibility = View.GONE

            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container2, fragment)
                .addToBackStack(null)
                .commit()
        }
        tvDrink = findViewById(R.id.tvDrink)
        tvDrink.setOnClickListener {
            showDrinkPicker()
        }
        tvSmoking = findViewById(R.id.tvSmoking)
        tvSmoking.setOnClickListener {
            showSmokingPicker()
        }
        tvDrugs = findViewById(R.id.tvDrugs)
        tvDrugs.setOnClickListener {
            showDrugsPicker()
        }
        tvPolitics = findViewById(R.id.tvPolitics)
        tvPolitics.setOnClickListener {
            showPoliticsPicker()
        }
        backButton.setOnClickListener {
            // Intent to navigate to the SecondActivity
            val intent = Intent(this, DescriptionActivity::class.java)
            startActivity(intent)
        }

    }
    private fun showReligionPicker() {
        val numberPicker = NumberPicker(this).apply {
            minValue = 0
            maxValue = religion.size - 1
            displayedValues = religion
            wrapSelectorWheel = true
        }

        AlertDialog.Builder(this).apply {
            setTitle("Who are you comfortable with")
            setView(numberPicker)
            setPositiveButton("OK") { _, _ ->
                /*Updates user religious input*/
                selectedReligion = religion[numberPicker.value]
                tvReligion.text = "${religion[numberPicker.value]} >"
                backButton.visibility = View.VISIBLE

            }
            setNegativeButton("Cancel", null)
        }.show()
    }
    private fun showInterestedPicker() {
        val numberPicker = NumberPicker(this).apply {
            minValue = 0
            maxValue = interested.size - 1
            displayedValues = interested
            wrapSelectorWheel = true
        }

        AlertDialog.Builder(this).apply {
            setTitle("Is there someone who you are looking for specifically?")
            setView(numberPicker)
            setPositiveButton("OK") { _, _ ->
                /*Updates user Ethnicity input*/
                selectedInterested = interested[numberPicker.value]
                tvInterest.text = "${interested[numberPicker.value]} >"
                backButton.visibility = View.VISIBLE

            }
            setNegativeButton("Cancel", null)
        }.show()
    }
    private fun showEthnicityPicker() {
        val numberPicker = NumberPicker(this).apply {
            minValue = 0
            maxValue = ethnicity.size - 1
            displayedValues = ethnicity
            wrapSelectorWheel = true
        }

        AlertDialog.Builder(this).apply {
            setTitle("Who are you comfortable with?")
            setView(numberPicker)
            setPositiveButton("OK") { _, _ ->
                /*Updates user Ethnicity input*/
                selectedEthnicity = ethnicity[numberPicker.value]
                tvEthnicity.text = "${ethnicity[numberPicker.value]} >"
                backButton.visibility = View.VISIBLE

            }
            setNegativeButton("Cancel", null)
        }.show()
    }
    private fun showDrinkPicker() {
        val numberPicker = NumberPicker(this).apply {
            minValue = 0
            maxValue = drinking.size - 1
            displayedValues = drinking
            wrapSelectorWheel = true
        }

        AlertDialog.Builder(this).apply {
            setTitle("Mind if they drink?")
            setView(numberPicker)
            setPositiveButton("OK") { _, _ ->
                /*Updates user Ethnicity input*/
                selectedDrink = drinking[numberPicker.value]
                tvDrink.text = "${drinking[numberPicker.value]} >"
                backButton.visibility = View.VISIBLE

            }
            setNegativeButton("Cancel", null)
        }.show()
    }
    private fun showDrugsPicker() {
        val numberPicker = NumberPicker(this).apply {
            minValue = 0
            maxValue = drugs.size - 1
            displayedValues = drugs
            wrapSelectorWheel = true
        }

        AlertDialog.Builder(this).apply {
            setTitle("Care if they take anything interesting?")
            setView(numberPicker)
            setPositiveButton("OK") { _, _ ->
                /*Updates user Ethnicity input*/
                selectedDrugs = drugs[numberPicker.value]
                tvDrugs.text = "${drugs[numberPicker.value]} >"
                backButton.visibility = View.VISIBLE

            }
            setNegativeButton("Cancel", null)
        }.show()
    }
    private fun showSmokingPicker() {
        val numberPicker = NumberPicker(this).apply {
            minValue = 0
            maxValue = smoking.size - 1
            displayedValues = smoking
            wrapSelectorWheel = true
        }

        AlertDialog.Builder(this).apply {
            setTitle("Do you smoke?")
            setView(numberPicker)
            setPositiveButton("OK") { _, _ ->
                /*Updates user Ethnicity input*/
                selectedSmoking = smoking[numberPicker.value]
                tvSmoking.text = "${smoking[numberPicker.value]} >"
                backButton.visibility = View.VISIBLE

            }
            setNegativeButton("Cancel", null)
        }.show()
    }
    private fun showPoliticsPicker() {
        val numberPicker = NumberPicker(this).apply {
            minValue = 0
            maxValue = politics.size - 1
            displayedValues = politics
            wrapSelectorWheel = true
        }

        AlertDialog.Builder(this).apply {
            setTitle("Care what side on the political spectrum they on?")
            setView(numberPicker)
            setPositiveButton("OK") { _, _ ->
                /*Updates user Ethnicity input*/
                selectedPolitics = politics[numberPicker.value]
                tvPolitics.text = "${politics[numberPicker.value]} >"
                backButton.visibility = View.VISIBLE

            }
            setNegativeButton("Cancel", null)
        }.show()
    }

    // Implementation of the OnDistanceSelectedListener interface
    override fun onDistanceSelected(distance: Int) {
        selectedDistance = distance  // Save the selected distance to the variable
        tvDistance.text = getString(R.string.selected_distance, distance)
        backButton.visibility = View.VISIBLE

    }
    override fun onAgeSelected(minAge: Int, maxAge: Int) {
        // Update the TextView to show the selected age range
        tvAge.text = "$minAge - $maxAge years old"

        // If you need to save the age range for later use, you can assign it to variables
        selectedMinAge = minAge
        selectedMaxAge = maxAge
        backButton.visibility = View.VISIBLE

        // Use selectedMinAge and selectedMaxAge as needed in your activity
    }

}
