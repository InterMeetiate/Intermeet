package com.intermeet.android.SignUp_SignIn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.intermeet.android.R

/**
 * A simple [Fragment] subclass that allows the user to input their occupation.
 */
class OccupationFragment : Fragment() {
    private var listener: OccupationListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_occupation, container, false)
        val editText = view.findViewById<EditText>(R.id.editTextOccupation)

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // When the 'Done' button on the keyboard is pressed
                listener?.onOccupationEntered(editText.text.toString())
                parentFragmentManager.popBackStack() // Return to the previous fragment/activity
                true // Indicate that the event has been handled
            } else {
                false // Unhandled events will continue to propagate
            }
        }
        return view
    }

    // Set the listener for occupation entry
    fun setOccupationListener(listener: UserInfoActivity) {
        this.listener = listener
    }

    // Interface for sending the occupation back to the activity
    interface OccupationListener {
        fun onOccupationEntered(occupation: String)
    }

    companion object {
        @JvmStatic
        fun newInstance() = OccupationFragment()
    }
}