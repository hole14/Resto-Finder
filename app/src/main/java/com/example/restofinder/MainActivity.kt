package com.example.restofinder

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restofinder.adapter.RestoAdapter
import com.example.restofinder.databinding.ActivityMainBinding
import com.example.restofinder.model.RestoModel
import com.example.restofinder.viewModel.RestoViewModel
import com.google.android.libraries.places.api.Places

class MainActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityMainBinding
    private lateinit var restoAdapter: RestoAdapter
    private lateinit var restoViewModel: RestoViewModel

    private var fullList: List<RestoModel> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        restoAdapter = RestoAdapter(emptyList())
        binding.rvResto.layoutManager = LinearLayoutManager(this)
        binding.rvResto.adapter = restoAdapter

        restoViewModel = ViewModelProvider(this)[RestoViewModel::class.java]
        restoViewModel.restoList.observe(this) { data ->
            restoAdapter.updateData(data)
            fullList = data
        }
        checkLocationPermission()

        binding.searchResto.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filteredList(s.toString())
            }

        })

    }

    private fun checkLocationPermission() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
            != android.content.pm.PackageManager.PERMISSION_GRANTED) {

            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
        } else {
            restoViewModel.loadResto()
        }
    }

    private fun filteredList(query: String) {
        val filteredList = fullList.filter {
            it.nama.contains(query, ignoreCase = true)
        }
        restoAdapter.updateData(filteredList)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            restoViewModel.loadResto()
        }
    }
}

