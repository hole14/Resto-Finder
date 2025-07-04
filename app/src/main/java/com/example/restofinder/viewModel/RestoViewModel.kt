package com.example.restofinder.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.restofinder.model.RestoModel
import com.example.restofinder.repository.RestoRepository
import kotlinx.coroutines.launch

class RestoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RestoRepository(application)
    private val _restoList = MutableLiveData<List<RestoModel>>()
    val restoList: LiveData<List<RestoModel>> = _restoList

    fun loadResto() {
        viewModelScope.launch {
            val result = repository.getNearbyResto()
            _restoList.postValue(result)
        }
    }
}