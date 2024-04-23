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
 * A simple [Fragment] subclass.
 * Use the [PronounFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PronounFragment : Fragment() {
    private var listener: PronounListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_pronoun, container, false)
        val editText = view.findViewById<EditText>(R.id.editTextPronoun)

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // When the 'Done' button on the keyboard is pressed
                listener?.onPronounEntered(editText.text.toString())
                parentFragmentManager.popBackStack() // Return to the previous fragment/activity
                true // Indicate that the event has been handled
            } else {
                false // Unhandled events will continue to propagate
            }
        }
        return view
    }
    // Set the listener for occupation entry
    fun setPronounListener(listener: UserInfoActivity) {
        this.listener = listener
    }

    // Interface for sending the occupation back to the activity
    interface PronounListener {
        fun onPronounEntered(pronoun: String)
    }
    companion object {
        @JvmStatic
        fun newInstance() = PronounFragment()
    }



}