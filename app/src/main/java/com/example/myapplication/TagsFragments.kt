package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.content.ContextCompat

class TagsFragment : Fragment() {

    private val selectedTags = mutableListOf<String>()
    private var listener: TagsSelectionListener? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tags_fragments, container, false)

        // Setup predefined tags (example for food tags)
        val foodTagsContainer = view.findViewById<GridLayout>(R.id.foodTagsContainer)
        val foodTags = resources.getStringArray(R.array.food_tags)
        foodTags.forEach { tag ->
            val tagView = createTagView(tag)
            tagView.setOnClickListener { toggleTagSelection(tag) }
            foodTagsContainer.addView(tagView)
        }
        val entertainTagsContainer = view.findViewById<GridLayout>(R.id.entertainmentTagsContainer)
        val entertainTags = resources.getStringArray(R.array.show_tags)
        entertainTags.forEach { tag ->
            val tagView = createTagView(tag)
            tagView.setOnClickListener { toggleTagSelection(tag) }
            entertainTagsContainer.addView(tagView)
        }
        val socializeTagsContainer = view.findViewById<GridLayout>(R.id.socialTagsContainer)
        val socializeTags = resources.getStringArray(R.array.social)
        socializeTags.forEach { tag ->
            val tagView = createTagView(tag)
            tagView.setOnClickListener { toggleTagSelection(tag) }
            socializeTagsContainer.addView(tagView)
        }
        val creativeTagsContainer = view.findViewById<GridLayout>(R.id.creativityTagsContainer)
        val creativeTags = resources.getStringArray(R.array.Creativity)
        creativeTags.forEach { tag ->
            val tagView = createTagView(tag)
            tagView.setOnClickListener { toggleTagSelection(tag) }
            creativeTagsContainer.addView(tagView)
        }
        val songTagsContainer = view.findViewById<GridLayout>(R.id.musicTagsContainer)
        val songTags = resources.getStringArray(R.array.music)
        songTags.forEach { tag ->
            val tagView = createTagView(tag)
            tagView.setOnClickListener { toggleTagSelection(tag) }
            songTagsContainer.addView(tagView)
        }
        val valueTagsContainer = view.findViewById<GridLayout>(R.id.valuesTagsContainer)
        val valueTags = resources.getStringArray(R.array.music)
        valueTags.forEach { tag ->
            val tagView = createTagView(tag)
            tagView.setOnClickListener { toggleTagSelection(tag) }
            valueTagsContainer.addView(tagView)
        }


        // Custom tag input
        val customTagInput = view.findViewById<EditText>(R.id.customTagInput)
        customTagInput.setOnEditorActionListener { v: TextView, actionId: Int, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val tag = v.text.toString()
                if (tag.isNotEmpty() && selectedTags.size < 6) {
                    selectedTags.add(tag)
                    // Update the UI accordingly
                    v.setText("")  // Clear the EditText after adding the tag
                }
                true // Return true because the event has been handled
            } else {
                false // Return false to let the system handle the event
            }
        }

        // Handle 'Done' button logic
        // Return the selected tags to UserInfoActivity

        return view
    }

    private fun createTagView(tag: String): View {
        // Create a new TextView for the tag
        val tagView = TextView(context).apply {
            text = tag
            gravity = Gravity.CENTER
            setTextColor(Color.BLACK) // Set text color to white
            textSize = 14f // Set the text size or use resources
            background = ContextCompat.getDrawable(context, R.drawable.tag_background)
            setPadding(1, 2, 2, 1) // Set padding (left, top, right, bottom)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                // Add layout parameters if necessary, e.g., margins
                if (this is ViewGroup.MarginLayoutParams) {
                    setMargins(0, 0, 0, 0) // Set margins (left, top, right, bottom)
                }
            }

            // Optionally, set a click listener or any other properties
        }

        // Return the constructed TextView
        return tagView
    }

    private fun toggleTagSelection(tag: String) {
        // Implementation to add/remove the tag from selectedTags and update UI accordingly
    }

    interface TagsSelectionListener {
        fun onTagsSelected(selectedTags: List<String>)
    }

    fun setTagsSelectionListener(listener: UserInfoActivity) {
        this.listener = listener
    }

    private fun doneSelectingTags() {
        listener?.onTagsSelected(selectedTags)
        // Possibly pop back the fragment or navigate accordingly
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btnBackToActivity).setOnClickListener {
            activity?.onBackPressed()
        }
    }
    companion object {
        @JvmStatic
        fun newInstance() = TagsFragment()
    }

    // Other methods...
}