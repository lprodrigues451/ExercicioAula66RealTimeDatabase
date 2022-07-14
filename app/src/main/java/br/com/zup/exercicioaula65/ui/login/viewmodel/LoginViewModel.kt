package br.com.zup.exercicioaula65.ui.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.zup.exercicioaula65.domain.model.User
import br.com.zup.exercicioaula65.domain.repository.AuthenticationRepository

class LoginViewModel : ViewModel() {
    private val authenticationRepository = AuthenticationRepository()

    private var _loginState = MutableLiveData<User>()
    val loginState: LiveData<User> = _loginState

    private var _errorState = MutableLiveData<String>()
    val errorState: LiveData<String> = _errorState

    fun validateDataUser(user: User) {
        when {
            user.email.isEmpty() -> {
                _errorState.value = "Favor informar email"
            }
            user.password.isEmpty() -> {
                _errorState.value = "Favor informar senha"
            }
            else -> {
                loginUser(user)
            }
        }
    }

    private fun loginUser(user: User) {
        try {
            authenticationRepository.loginUser(
                user.email,
                user.password
            ).addOnSuccessListener {
                _loginState.value = user
            }.addOnFailureListener {
                _errorState.value = "Não foi possivel realizar o login!" + it.message
            }
        } catch (ex: Exception) {
            _errorState.value = ex.message
        }
    }
}