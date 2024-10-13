package com.example.mvvmexercise.model

import androidx.lifecycle.MutableLiveData

class UserRepository {
    private val users = MutableLiveData<List<User>>();

    fun login(username: String, password: String): Boolean {

        return true
    }
}

data class User(val username: String, val email: String)