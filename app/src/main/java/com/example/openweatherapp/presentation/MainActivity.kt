package com.example.openweatherapp.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.openweatherapp.R
import com.example.openweatherapp.databinding.ActivityMainBinding
import com.example.openweatherapp.utility.Constants.PREF_CITY_KEY
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var mainFragment: MainFragment
    private lateinit var weatherFragment: LocalWeatherFragment
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private var currentLocation = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this@MainActivity
        viewModel = ViewModelProvider(this@MainActivity)[MainViewModel::class.java]
        val fragmentManager = supportFragmentManager
        setUpFragment(fragmentManager)
        requestLocationPermission()
    }

    private fun setUpFragment(fragmentManager: FragmentManager) {
        mainFragment = MainFragment()
        weatherFragment = LocalWeatherFragment()
        val fragment = if (viewModel.getFromSharedPreferences(this@MainActivity.applicationContext, PREF_CITY_KEY, "").isBlank() && currentLocation.isBlank()) {
            mainFragment
        }  else {
            if (!currentLocation.isBlank()) {
                viewModel.addToSharedPreferences(this@MainActivity.applicationContext, PREF_CITY_KEY, currentLocation)
            }
            weatherFragment
        }

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            getCurrentLocation()
        }
    }

    private fun getCurrentLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    getCityFromLocation(location)
                } else {
                    currentLocation = ""
                    Toast.makeText(this@MainActivity.applicationContext, "Check location Service", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("ERROR:", "Error getting location: ${exception.message}", exception)
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
                return
            }

            getCurrentLocation()
        } else {
            Toast.makeText(this@MainActivity.applicationContext, "Check location Service", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCityFromLocation(location: Location) {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                val cityName = addresses[0].locality
                Log.d("CITY", "City: $cityName")
                currentLocation = cityName
            } else {
                Toast.makeText(this@MainActivity.applicationContext, "No Addresses Found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}