package com.intermeet.android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText


class EditPronounFragment : Fragment() {
    private var listener: editPronounListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_pronoun, container, false)
        val editText = view.findViewById<EditText>(R.id.editTextPronoun)

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // When the 'Done' button on the keyboard is pressed
                listener?.onEditPronounEntered(editText.text.toString())
                parentFragmentManager.popBackStack() // Return to the previous fragment/activity
                true // Indicate that the event has been handled
            } else {
                false // Unhandled events will continue to propagate
            }
        }
        return view
    }

    fun editPronounListener(listener: EditProfile) {
        this.listener = listener
    }

    // Interface for sending the occupation back to the activity
    interface PronounListener {
        fun onEditPronounEntered(pronoun: String)
    }
    companion object {
        @JvmStatic
        fun newInstance() = PronounFragment()
    }



}