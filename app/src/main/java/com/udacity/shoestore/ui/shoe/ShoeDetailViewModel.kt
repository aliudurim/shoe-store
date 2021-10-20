package com.udacity.shoestore.ui.shoe

import androidx.lifecycle.*
import com.udacity.shoestore.models.Shoe
import kotlinx.coroutines.launch

class ShoeDetailViewModel : ViewModel() {

    sealed class Event {
        data class SaveShoe(val shoe: Shoe) : Event()
        object Cancel : Event()
    }

    private var _shoeDetailEvent = MutableLiveData<Event>()
    val shoeDetailEvent: LiveData<Event>
        get() = _shoeDetailEvent

    /**
     * Two way data binding
     */
    val shoeNameTextContent = MutableLiveData<String>()
    val companyTextContent = MutableLiveData<String>()
    val shoeSizeTextContent = MutableLiveData<String>()
    val descriptionTextContent = MutableLiveData<String>()

    fun createShoe() {
        viewModelScope.launch {
            val shoe = Shoe(
                shoeNameTextContent.value!!,
                shoeSizeTextContent.value!!.toDouble(),
                companyTextContent.value!!,
                descriptionTextContent.value!!
            )
            _shoeDetailEvent.value = Event.SaveShoe(shoe)
        }
    }

    fun cancel() {
        _shoeDetailEvent.value = Event.Cancel
    }

    fun clearEvent() {
        _shoeDetailEvent.value = null
    }

    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ShoeDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ShoeDetailViewModel() as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}