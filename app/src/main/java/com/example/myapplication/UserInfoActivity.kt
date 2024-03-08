package com.example.myapplication

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class UserInfoActivity : AppCompatActivity(), OccupationFragment.OccupationListener,
    PronounFragment.PronounListener, TagsFragment.TagsSelectionListener {
    private lateinit var tvGender: TextView
    private lateinit var tvHeight: TextView
    private lateinit var tvReligion: TextView
    private lateinit var tvEthnicity: TextView
    private lateinit var tvJob: TextView // TextView for occupation
    private lateinit var tvSex: TextView
    private lateinit var tvPronoun: TextView
    private lateinit var tvOccupation: TextView
    private lateinit var tagsDisplay: TextView // TextView to display selected tags
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
    private val sexuality = arrayOf(
        "Straight", "Gay", "Lesbian", "Bisexual", "Asexual", "Pansexual", "Queer", "Other")

    private var selectedGender: String? = null
    private var selectedHeight: String? = null
    private var selectedReligion: String? = null
    private var selectedEthnicity: String? = null
    private var selectedJob: String? = null // Variable to store the occupation
    private var selectedSex: String? = null
    private var selectedPronoun: String? = null

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
        tvJob= findViewById(R.id.tvJob)
        tvJob.setOnClickListener {
            tvJob.visibility = View.GONE
            val occupationFragment = OccupationFragment().also {
                // "this" is UserInfoActivity which implements OccupationListener
                it.setOccupationListener(this)
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container1, occupationFragment)
                .addToBackStack(null)
                .commit()
        }
        tvSex = findViewById(R.id.tvSex)
        tvSex.setOnClickListener {
            showSexualityPicker()
        }
        tvPronoun= findViewById(R.id.tvPronoun)
        tvPronoun.setOnClickListener {
            tvPronoun.visibility = View.GONE
            val pronounFragment = PronounFragment().also {
                it.setPronounListener(this)
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container2, pronounFragment)
                .addToBackStack(null)
                .commit()
        }
        val tagsFragment = TagsFragment().also {
            it.setTagsSelectionListener(this)
        }
        val btnAddTags = findViewById<Button>(R.id.addTagButton)
        btnAddTags.setOnClickListener {
            val tagsFragment = TagsFragment()
            // Assuming you have a FrameLayout with the ID fragment_container in your layout
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container3, tagsFragment)
                .addToBackStack(null) // Add this transaction to the back stack
                .commit()
        }

    }
    override fun onOccupationEntered(occupation: String) {
        tvJob.text = "$occupation >"
        selectedJob = occupation
        tvJob.visibility = View.VISIBLE
    }
    override fun onPronounEntered(pronoun: String) {
        tvPronoun.text = "$pronoun >"
        selectedPronoun = pronoun
        tvPronoun.visibility = View.VISIBLE
    }

    override fun onTagsSelected(selectedTags: List<String>) {
        // This method will be called when the tags are selected in the fragment.
        // Update your UI or data model accordingly.
        tagsDisplay.text = selectedTags.joinToString(", ")
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
    private fun showSexualityPicker() {
        val numberPicker = NumberPicker(this).apply {
            minValue = 0
            maxValue = sexuality.size - 1
            displayedValues = sexuality
            wrapSelectorWheel = true
        }

        AlertDialog.Builder(this).apply {
            setTitle("Select Your Sexuality")
            setView(numberPicker)
            setPositiveButton("OK") { _, _ ->
                /*Updates user Sexuality input*/
                selectedSex = sexuality[numberPicker.value]
                tvSex.text = "${sexuality[numberPicker.value]} >"
            }
            setNegativeButton("Cancel", null)
        }.show()
    }
    override fun onResume() {
        super.onResume()
        tvJob.visibility = View.VISIBLE
        tvPronoun.visibility = View.VISIBLE
        // similarly for the button if it's a separate view
    }
}
interface OccupationListener {
    fun onOccupationEntered(occupation: String)
}
interface PronounListener {
    fun onPronounEntered(pronoun: String)
}