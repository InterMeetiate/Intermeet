package com.intermeet.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController

class PaymentsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.settings_payments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        val addCardBox: LinearLayout = view.findViewById(R.id.add_card_box)

        toolbar.setNavigationOnClickListener {
            // Use NavController to navigate up in the navigation graph
            findNavController().navigateUp()
        }

        addCardBox.setOnClickListener {
            // Handle add card box click, navigate to BirthdayFragment (or Activity if necessary)
            // Assuming you're using Navigation Component for fragment transactions
            val action = PaymentsFragmentDirections.actionPaymentsFragmentToSettingsFragment()
            findNavController().navigate(action)
        }
    }
}
