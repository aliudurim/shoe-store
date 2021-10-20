package com.udacity.shoestore.ui.shoe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.shoestore.models.Shoe

class ShoeListViewModel : ViewModel() {

    private val _shoeListLiveData = MutableLiveData<List<Shoe>>()
    val shoeListLiveData: LiveData<List<Shoe>>
        get() = _shoeListLiveData

    fun addShoe(shoe: Shoe) {
        val a = _shoeListLiveData.value ?: mutableListOf()
        _shoeListLiveData.value = a.plus(shoe)
    }
}