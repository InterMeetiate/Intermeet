package com.intermeet.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.appcompat.widget.Toolbar

class SettingsFragment : Fragment() {
    companion object {
        private const val TAG = "SettingFragment"
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        val emailButton: Button = view.findViewById(R.id.Email)
        val paymentsButton: Button = view.findViewById(R.id.Payments)
        val phoneNumberButton: Button = view.findViewById(R.id.PhoneNumber)

        // Assume navController is set up with the toolbar for navigation
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Navigate using NavController
        emailButton.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_profileEmailFragment)
        }

        paymentsButton.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_paymentsFragment)
        }

        phoneNumberButton.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_phoneNumberFragment)
        }
    }
}
