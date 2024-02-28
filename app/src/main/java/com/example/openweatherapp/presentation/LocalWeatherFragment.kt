package com.example.openweatherapp.presentation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.openweatherapp.R
import com.example.openweatherapp.databinding.FragmentLocalWeatherBinding
import com.example.openweatherapp.utility.Constants.PREF_CITY_KEY

class LocalWeatherFragment : Fragment() {
    private lateinit var binding: FragmentLocalWeatherBinding
    private lateinit var viewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_local_weather, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        val arg = arguments?.getString(PREF_CITY_KEY, "")?: ""
        val city = viewModel.getFromSharedPreferences(requireContext(), PREF_CITY_KEY, "")
        val str = arg.ifBlank {
            city
        }
        viewModel.apply {
            getWeather(str)
            weatherData.observe(requireActivity()) { item ->
                if (item != null && this@LocalWeatherFragment.isAdded) {
                    binding.cityLabel.text = str
                    addToSharedPreferences(requireContext(), PREF_CITY_KEY, str)
                }
            }
        }

        binding.btSearch.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, MainFragment())
                .addToBackStack("MAINFRAGMENT")
                .commit()
        }
        return binding.root
    }
}