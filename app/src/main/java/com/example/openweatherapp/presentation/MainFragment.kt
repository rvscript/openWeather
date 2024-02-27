package com.example.openweatherapp.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.openweatherapp.R
import com.example.openweatherapp.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var _binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        _binding.lifecycleOwner = viewLifecycleOwner
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        viewModel.weatherData.observe(requireActivity()) {data->
            if (data!=null) {
                val value = data
                Log.d("data", "onCreateView: $value")
            }
        }
/*
val countries: SortedSet<String> = TreeSet()
        for (locale in Locale.getAvailableLocales()) {
            if (!TextUtils.isEmpty(locale.displayCountry)) {
                countries.add(locale.displayCountry)
            }
        }
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, countries.toList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        binding.countrySpinner.adapter = adapter
 */
        return _binding.root
    }
}