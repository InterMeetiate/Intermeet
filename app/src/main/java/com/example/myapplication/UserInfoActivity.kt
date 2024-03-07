package com.example.myapplication

import android.app.AlertDialog
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
/*test*/
class UserInfoActivity : AppCompatActivity() {
    private lateinit var tvGender: TextView
    private lateinit var tvHeight: TextView
    private lateinit var tvReligion: TextView
    private lateinit var tvEthnicity: TextView
    private val genders = arrayOf("Male", "Female", "Nonbinary", "Other")
    private val heights = arrayOf(
        "3'0","3'1","3'2","3'3","3'4","3'5","3'6","3'7","3'8","3'9","3'10","3'11",
        "4'0","4'1","4'2","4'3","4'4","4'5","4'6","4'7","4'8","4'9","4'10","4'11",
        "5'0","5'1","5'2","5'3","5'4","5'5","5'6","5'7","5'8","5'9","5'10","5'11",
        "6'0","6'1","6'2","6'3","6'4","6'5","6'6","6'7","6'8","6'9","6'10","6'11",
        "7'0")
    private val religion = arrayOf(
        "Agnostic", "Atheist", "Buddhist", "Catholic", "Christian", "Hindu", "Jewish", "Muslim",
        "Sikh", "Spiritual", "Other", "Prefer not to say")
    private val ethnicity = arrayOf(
        "Black/African Descent", "East Asian", "Hispanic/Latino", "Middle Eastern", "Native American",
        "Pacific Islander", "South Asian", "Southeast Asian", "White/Caucasian", "Other", "Prefer not to say")
    private var selectedGender: String? = null
    private var selectedHeight: String? = null
    private var selectedReligion: String? = null
    private var selectedEthnicity: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        tvGender = findViewById(R.id.tvGender)
        tvGender.setOnClickListener {
            showGenderPicker()
        }
        tvHeight = findViewById(R.id.tvHeight)
        tvHeight.setOnClickListener {
            showHeightPicker()
        }
        tvReligion = findViewById(R.id.tvReligion)
        tvReligion.setOnClickListener {
            showReligionPicker()
        }
        tvEthnicity = findViewById(R.id.tvEthnicity)
        tvEthnicity.setOnClickListener {
            showEthnicityPicker()
        }
    }

    private fun showGenderPicker() {
        val numberPicker = NumberPicker(this).apply {
            minValue = 0
            maxValue = genders.size - 1
            displayedValues = genders
            wrapSelectorWheel = true
        }

        AlertDialog.Builder(this).apply {
            setTitle("Select Your Gender")
            setView(numberPicker)
            setPositiveButton("OK") { _, _ ->
                /*Updates user gender input*/
                selectedGender = genders[numberPicker.value]
                tvGender.text = "${genders[numberPicker.value]} >"
            }
            setNegativeButton("Cancel", null)
        }.show()
    }
    private fun showHeightPicker() {
        val numberPicker = NumberPicker(this).apply {
            minValue = 0
            maxValue = heights.size - 1
            displayedValues = heights
            wrapSelectorWheel = true
        }

        AlertDialog.Builder(this).apply {
            setTitle("Select Your Height")
            setView(numberPicker)
            setPositiveButton("OK") { _, _ ->
                /*Updates user height input*/
                selectedHeight = heights[numberPicker.value]
                tvHeight.text = "${heights[numberPicker.value]} >"
            }
            setNegativeButton("Cancel", null)
        }.show()
    }

    private fun showReligionPicker() {
        val numberPicker = NumberPicker(this).apply {
            minValue = 0
            maxValue = religion.size - 1
            displayedValues = religion
            wrapSelectorWheel = true
        }

        AlertDialog.Builder(this).apply {
            setTitle("Select Your Religious Belief")
            setView(numberPicker)
            setPositiveButton("OK") { _, _ ->
                /*Updates user religious input*/
                selectedReligion = religion[numberPicker.value]
                tvReligion.text = "${religion[numberPicker.value]} >"
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
            setTitle("Select Your Ethnicity")
            setView(numberPicker)
            setPositiveButton("OK") { _, _ ->
                /*Updates user Ethnicity input*/
                selectedEthnicity = ethnicity[numberPicker.value]
                tvEthnicity.text = "${ethnicity[numberPicker.value]} >"
            }
            setNegativeButton("Cancel", null)
        }.show()
    }
}