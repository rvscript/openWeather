package com.example.openweatherapp.presentation

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
            // create methods to bind values instead of all in observer
            weatherData.observe(requireActivity()) { item ->
                if (item != null && this@LocalWeatherFragment.isAdded) {
                    binding.cityLabel.text = str
                    val fahTemp = viewModel.kelvinToFahrenheit(item.main.temp)
                    val fahFormatted = String.format("%.1f", fahTemp)
                    val tvTempStr = "$fahFormatted F"?: ""
                    binding.tvTemp.text = tvTempStr
                    binding.tvDescription.text = item.weather[0].description
                    val tvHumidityStr = "Humidity ${item.main.humidity}%"
                    binding.tvHumidity.text = tvHumidityStr
                    val windDesc = "Wind ${item.wind.deg} deg, ${item.wind.speed} MPH"
                    binding.tvWind.text = windDesc
                    addToSharedPreferences(requireContext(), PREF_CITY_KEY, str)
                    val unixTimeRise = item.sys.sunrise
                    val sunrise = viewModel.convertUnixTimeToHoursMinutes(unixTimeRise.toLong())
                    val sunRiseStr = "SunRise $sunrise"
                    binding.tvSunrise.text = sunRiseStr
                    val unixTimeSet = item.sys.sunset
                    val sunset = viewModel.convertUnixTimeToHoursMinutes(unixTimeSet.toLong())
                    val sunSetStr = "Sunset $sunset"
                    binding.tvSunset.text = sunSetStr
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