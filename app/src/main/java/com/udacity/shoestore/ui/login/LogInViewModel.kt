package com.udacity.shoestore.ui.login

import androidx.lifecycle.*
import com.udacity.shoestore.models.UserModel
import com.udacity.shoestore.persistence.ShoePreferences
import com.udacity.shoestore.utils.isValidEmail
import com.udacity.shoestore.utils.mediatorLiveData
import kotlinx.coroutines.launch

class LogInViewModel : ViewModel() {

    sealed class Event {
        data class Navigate(val navigate: Boolean, val message: String? = null) : Event()
    }

    private var _navigateToWelcomeScreen = MutableLiveData<Event>()
    val navigateToWelcomeScreen: LiveData<Event>
        get() = _navigateToWelcomeScreen

    /**
     * Two way data binding
     */
    val emailTextContent = MutableLiveData<String>()
    val passwordTextContent = MutableLiveData<String>()

    private val hasValidEmail: LiveData<Boolean>
        get() = Transformations.map(emailTextContent) {
            isValidEmail(it)
        }

    private val hasValidPassword: LiveData<Boolean>
        get() = Transformations.map(passwordTextContent) {
            !it.isNullOrBlank()
        }

    val allowToLogIn: LiveData<Boolean> =
        mediatorLiveData(listOf(hasValidEmail, hasValidPassword))

    val allowToCreateAccount: LiveData<Boolean> =
        mediatorLiveData(listOf(hasValidEmail, hasValidPassword))

    fun logIn() {
        viewModelScope.launch {
            val userModel = UserModel(emailTextContent.value!!, passwordTextContent.value!!)
            if (ShoePreferences.containUser(userModel) != null) {
                navigateToWelcomeScreen()
            } else {
                _navigateToWelcomeScreen.value = Event.Navigate(false, "Please create user")
            }
        }
    }

    fun createAccount() {
        viewModelScope.launch {
            val userModel = UserModel(emailTextContent.value!!, passwordTextContent.value!!)
            if (ShoePreferences.containUser(userModel) != null) {
                _navigateToWelcomeScreen.value =
                    Event.Navigate(false, "Please log in with this user")
            } else {
                ShoePreferences.createUser(userModel)
                navigateToWelcomeScreen()
            }
        }
    }

    private fun navigateToWelcomeScreen() {
        viewModelScope.launch {
            _navigateToWelcomeScreen.value = Event.Navigate(true)
        }
    }

    fun clearNavigateData() {
        _navigateToWelcomeScreen.value = null
    }


    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LogInViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LogInViewModel() as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}