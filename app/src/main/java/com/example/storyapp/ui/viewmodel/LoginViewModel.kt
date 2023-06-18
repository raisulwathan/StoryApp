package com.example.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.LoginResponse
import retrofit2.Call
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _message = MutableLiveData<String>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _userLogin = MutableLiveData<LoginResponse>()

    val message: LiveData<String> = _message
    val isLoading: LiveData<Boolean> = _isLoading
    val userlogin: LiveData<LoginResponse> = _userLogin

    var isError: Boolean = false

    fun getLoginUser(email: String, password: String) {
        _isLoading.value = true
        val api = ApiConfig.getApiService().loginUser(email, password)
        api.enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful) {
                    isError = false
                    _userLogin.value = responseBody!!
                    _message.value = "Login as ${_userLogin.value!!.loginResult.name}"
                } else {
                    isError = true
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                isError = true
                _isLoading.value = false
                _message.value = t.message.toString()
            }
        })
    }
}