package com.example.openweatherapp.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.openweatherapp.R
import com.example.openweatherapp.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var _binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var fragmentWeather: LocalWeatherFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        fragmentWeather = LocalWeatherFragment()
        _binding.lifecycleOwner = viewLifecycleOwner
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        viewModel.weatherData.observe(requireActivity()) { data ->
            if (data != null) {
                val value = data
                Log.d("data", "onCreateView: $value")
            }
        }
        createCityList()
        setUpEditText()
        return _binding.root
    }

    private fun createCityList() {
        CoroutineScope(IO).launch {
            viewModel.getListOfCities(requireContext())
        }
    }

    private fun setUpEditText() {
        viewModel.cityList.observe(requireActivity()) { list ->
            if (!list.isNullOrEmpty()) {
                val origList = ArrayList<String>()
                origList.addAll(list)
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    list
                )
                _binding.listView.adapter = adapter

                _binding.listView.setOnItemClickListener { parent, view, position, id ->
                    val selectedItem = list[position]
                    val bundle = Bundle().apply {
                        putString("CITY", selectedItem)
                    }

                    fragmentWeather.apply {
                        arguments = bundle
                    }

                    parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragmentWeather)
                        .addToBackStack("fragmentWeather")
                        .commit()
                }

                _binding.searchEditText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        list.clear()
                        list.addAll(origList)
                        filter(s.toString(), adapter, list)
                    }
                })
            }
        }
    }

    private fun filter(text: String, adapter: ArrayAdapter<String>, list: MutableList<String>) {
        val filteredItems = ArrayList<String>()
        for(item in list) {
            if (item.contains(text, ignoreCase = true)) {
                filteredItems.add(item)
            }
        }
        adapter.clear()
        adapter.addAll(filteredItems)
    }
}