package com.udacity.shoestore.ui.login

import androidx.lifecycle.*
import com.udacity.shoestore.models.UserModel
import com.udacity.shoestore.persistence.ShoePreferences
import com.udacity.shoestore.utils.isValidEmail
import com.udacity.shoestore.utils.mediatorLiveData
import kotlinx.coroutines.launch
import java.util.regex.Matcher
import java.util.regex.Pattern

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

    private val hasEightCharacter: LiveData<Boolean>
        get() = Transformations.map(passwordTextContent) {
            val eight: Pattern = Pattern.compile(".{8}")
            val hasEight: Matcher = eight.matcher(it)
            hasEight.find()
        }

    private val hasSpecialCharacter: LiveData<Boolean>
        get() = Transformations.map(passwordTextContent) {
            val special: Pattern = Pattern.compile("[!@#\$%&*()_+=|<>?{}\\\\[\\\\]~-]")
            val hasSpecial: Matcher = special.matcher(it)
            hasSpecial.find()
        }

    private val hasDigitCharacter: LiveData<Boolean>
        get() = Transformations.map(passwordTextContent) {
            val digit: Pattern = Pattern.compile("[0-9]")
            val hasDigit: Matcher = digit.matcher(it)
            hasDigit.find()
        }

    private val hasValidPassword: LiveData<Boolean>
        get() = mediatorLiveData(listOf(hasEightCharacter, hasSpecialCharacter, hasDigitCharacter))

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