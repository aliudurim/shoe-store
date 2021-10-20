package com.udacity.shoestore.persistence

import com.udacity.shoestore.models.UserModel

object ShoePreferences {
    private val users: MutableList<UserModel> = mutableListOf()

    fun containUser(user: UserModel): UserModel? {
        return users.find { it == user }
    }

    fun createUser(user: UserModel) {
        users.add(user)
    }
}