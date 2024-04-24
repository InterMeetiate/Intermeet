package com.intermeet.android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText


class EditOccupation : Fragment() {
    private var listener: editOccupationListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_occupation, container, false)
        val editText = view.findViewById<EditText>(R.id.editTextOccupation)

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // When the 'Done' button on the keyboard is pressed
                listener?.onEditOccupationEntered(editText.text.toString())
                parentFragmentManager.popBackStack() // Return to the previous fragment/activity
                true // Indicate that the event has been handled
            } else {
                false // Unhandled events will continue to propagate
            }
        }
        return view
    }

    fun setEditOccupationListener(listener: EditProfile) {
        this.listener = listener
    }

    // Interface for sending the occupation back to the activity
    interface editOccupationListener {
        fun onEditOccupationEntered(occupation: String) // Ensure this is the correct method signature
    }

    companion object {
        @JvmStatic
        fun newInstance() = EditOccupation()
        fun setOccupationListener(editProfile: EditProfile) {

        }
    }
}