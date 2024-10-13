package com.example.mvvmexercise.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmexercise.model.UserRepository

class MainViewModel : ViewModel() {

    private val userRepository = UserRepository()
    private val loginStatus = MutableLiveData<Boolean>()

    fun login(username: String, password: String) {
        loginStatus.value = userRepository.login(username, password)
    }

    fun getLoginStatus(): LiveData<Boolean> {
        return loginStatus;
    }
}