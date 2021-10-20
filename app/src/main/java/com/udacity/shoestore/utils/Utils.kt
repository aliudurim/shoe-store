package com.udacity.shoestore.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import java.util.regex.Pattern

private val EMAIL_ADDRESS_PATTERN = Pattern.compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)

fun isValidEmail(str: String): Boolean {
    return EMAIL_ADDRESS_PATTERN.matcher(str).matches()
}

fun mediatorLiveData(inputs: List<LiveData<Boolean>>): LiveData<Boolean> {
    val result = MediatorLiveData<Boolean>()
    val doCalculation = Observer<Boolean> {
        result.value = inputs.all { it.value == true }
    }
    inputs.forEach {
        result.addSource(it, doCalculation)
    }
    return result
}