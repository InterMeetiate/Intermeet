package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.slider.Slider
import com.example.myapplication.databinding.FragmentDistanceBinding



class DistanceFragment : Fragment() {

    var listener: OnDistanceSelectedListener? = null
    private var _binding: FragmentDistanceBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDistanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSlider()
        // Here, let's assume you have a button to confirm the selection
        binding.backButton.setOnClickListener {
            listener?.onDistanceSelected(binding.distanceSlider.value.toInt())
            requireActivity().onBackPressed() // Go back to the previous Activity
        }
    }

    private fun setupSlider() {
        with(binding) {
            // Initialize the slider range and value
            distanceSlider.valueFrom = 1f
            distanceSlider.valueTo = 100f
            distanceSlider.value = 1f

            // Update the text view initially
            distanceText.text = getString(R.string.selected_distance, distanceSlider.value.toInt())

            // Set a listener to update the text view as the slider value changes
            distanceSlider.addOnChangeListener { _, value, _ ->
                distanceText.text = getString(R.string.selected_distance, value.toInt())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnDistanceSelectedListener {
        fun onDistanceSelected(distance: Int)
    }
    fun setDistanceListener(listener: PreferenceActivity) {
        this.listener = listener
    }


}


