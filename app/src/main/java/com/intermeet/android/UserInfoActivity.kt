package com.intermeet.android

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.activity.viewModels

class UserInfoActivity : AppCompatActivity(), OccupationFragment.OccupationListener,
    PronounFragment.PronounListener, TagsFragment.OnTagsSelectedListener {
    val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var backButton: Button
    private lateinit var tvGender: TextView
    private lateinit var tvHeight: TextView
    private lateinit var tvReligion: TextView
    private lateinit var tvEthnicity: TextView
    private lateinit var tvJob: TextView // TextView for occupation
    private lateinit var tvSex: TextView
    private lateinit var tvPronoun: TextView
    private lateinit var tvOccupation: TextView
    private lateinit var tagsDisplay: TextView // TextView to display selected tags
    private lateinit var tvDrink: TextView
    private lateinit var tvDrugs: TextView
    private lateinit var tvSmoking: TextView
    private lateinit var tvPolitics: TextView
    private lateinit var btnNavigateFragment: TextView
    private var selectedTags: List<String> = listOf()
    private val genders = arrayOf("Male", "Female", "Nonbinary", "Trans", "Other")
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
        "Straight", "Gay", "Lesbian", "Bisexual", "Asexual", "Pansexual", "Queer", "Other", "Figuring it out", "Prefer not to say")
    private val drinking = arrayOf(
        "Yes", "No", "Prefer not to say"
    )
    private val drugs = arrayOf(
        "Yes", "No", "Prefer not to say"
    )
    private val smoking = arrayOf(
        "Yes", "No", "Prefer not to say"
    )
    private val politics = arrayOf(
        "Liberal", "Moderate", "Conservative", "Not Political", "Other", "Prefer not to say"
    )

    private var selectedGender: String? = null
    private var selectedHeight: String? = null
    private var selectedReligion: String? = null
    private var selectedEthnicity: String? = null
    private var selectedJob: String? = null // Variable to store the occupation
    private var selectedSex: String? = null
    private var selectedPronoun: String? = null
    private var selectedDrink: String? = null
    private var selectedDrugs: String? = null
    private var selectedSmoking: String? = null
    private var selectedPolitics: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user_info)
        backButton = findViewById(R.id.next_button)


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
                backButton.visibility = View.GONE
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
                backButton.visibility = View.GONE

            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container2, pronounFragment)
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

        btnNavigateFragment = findViewById(R.id.addTagButton) // Replace with your actual button ID
        btnNavigateFragment.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container3, TagsFragment())
            transaction.addToBackStack(null) // Add this transaction to the back stack (optional)
            transaction.commit()
            backButton.visibility = View.GONE


        }

        backButton.setOnClickListener {
            // Intent to navigate to the SecondActivity
            val intent = Intent(this, PreferenceActivity::class.java)
            startActivity(intent)
        }



    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
    override fun onOccupationEntered(occupation: String) {
        tvJob.text = "$occupation >"
        selectedJob = occupation
        tvJob.visibility = View.VISIBLE
        backButton.visibility = View.VISIBLE

    }
    override fun onPronounEntered(pronoun: String) {
        tvPronoun.text = "$pronoun >"
        selectedPronoun = pronoun
        tvPronoun.visibility = View.VISIBLE
        backButton.visibility = View.VISIBLE

    }
    override fun onTagsSelected(tags: List<String>) {
        selectedTags = tags
        // Assuming you have a LinearLayout (tagsContainer) to add tags TextViews
        val tagsContainer: GridLayout = findViewById(R.id.customTagsContainer)
        tagsContainer.removeAllViews() // Clear previous tags if any

        tags.forEach { tag ->
            val textView = TextView(this).apply {
                text = tag
                gravity = Gravity.CENTER
                textSize = 14f // Set the text size or use resources
                setTextColor(Color.BLACK)
                background = ContextCompat.getDrawable(context, R.drawable.tag_background)
                setPadding(10, 10, 10, 10) // Set padding (left, top, right, bottom)

                layoutParams = GridLayout.LayoutParams().apply {
                    // Add layout parameters if necessary, e.g., margins
                    if (this is ViewGroup.MarginLayoutParams) {
                        setMargins(20, 20, 20, 20) // Set margins (left, top, right, bottom)
                    }
                }
                // Add styling here if needed
            }
            tagsContainer.addView(textView)
            sharedViewModel.setSelectedTags(tags)
        }
        val btnNavigateFragment = findViewById<TextView>(R.id.addTagButton)
        btnNavigateFragment.visibility = View.VISIBLE
        tagsContainer.visibility = View.VISIBLE
        backButton.visibility = View.VISIBLE

        Log.d("UserInfoActivity", "Tags received: $tags")
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
    private fun showDrinkPicker() {
        val numberPicker = NumberPicker(this).apply {
            minValue = 0
            maxValue = drinking.size - 1
            displayedValues = drinking
            wrapSelectorWheel = true
        }

        AlertDialog.Builder(this).apply {
            setTitle("Do you drink?")
            setView(numberPicker)
            setPositiveButton("OK") { _, _ ->
                /*Updates user Ethnicity input*/
                selectedDrink = drinking[numberPicker.value]
                tvDrink.text = "${drinking[numberPicker.value]} >"
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
            setTitle("Do you take anything interesting?")
            setView(numberPicker)
            setPositiveButton("OK") { _, _ ->
                /*Updates user Ethnicity input*/
                selectedDrugs = drugs[numberPicker.value]
                tvDrugs.text = "${drugs[numberPicker.value]} >"
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
            setTitle("What side on the political spectrum you on?")
            setView(numberPicker)
            setPositiveButton("OK") { _, _ ->
                /*Updates user Ethnicity input*/
                selectedPolitics = politics[numberPicker.value]
                tvPolitics.text = "${politics[numberPicker.value]} >"
            }
            setNegativeButton("Cancel", null)
        }.show()
    }
    override fun onResume() {
        super.onResume()
        backButton.visibility = View.VISIBLE
        tvJob.visibility = View.VISIBLE
        tvPronoun.visibility = View.VISIBLE
        println("here")
        val btnNavigateFragment = findViewById<TextView>(R.id.addTagButton) // Adjust ID as needed
        btnNavigateFragment.visibility = View.VISIBLE // Make the button visible again
        // similarly for the button if it's a separate view

    }


}
interface OccupationListener {
    fun onOccupationEntered(occupation: String)
}
interface PronounListener {
    fun onPronounEntered(pronoun: String)
}
interface OnTagsSelectedListener {
    fun onTagsSelected(tags: List<String>)
}

// In your fragment

