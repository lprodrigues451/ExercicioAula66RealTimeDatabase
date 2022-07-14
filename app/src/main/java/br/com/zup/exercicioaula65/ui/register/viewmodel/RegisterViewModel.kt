package br.com.zup.exercicioaula65.ui.register.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.zup.exercicioaula65.domain.model.User
import br.com.zup.exercicioaula65.domain.repository.AuthenticationRepository

class RegisterViewModel : ViewModel() {
    private val authenticationRepository = AuthenticationRepository()

    private var _registerState = MutableLiveData<User>()
    val registerState: LiveData<User> = _registerState

    private var _errorState = MutableLiveData<String>()
    val errorState: LiveData<String> = _errorState

    fun validateDataUser(user: User) {
        when {
            user.name.isEmpty() -> {
                _errorState.value = "Favor informar nome"
            }
            user.email.isEmpty() -> {
                _errorState.value = "Favor informar email"
            }
            user.password.isEmpty() -> {
                _errorState.value = "Favor informar senha"
            }
            else -> {
                registerUser(user)
            }
        }
    }

    private fun registerUser(user: User) {
        try {
            authenticationRepository.registerUser(
                user.email,
                user.password
            ).addOnSuccessListener {

                authenticationRepository.updateUserProfile(user.name)?.addOnSuccessListener {
                    _registerState.value = user
                }

            }.addOnFailureListener {
                _errorState.value = "Erro ao criar usuário!" + it.message
            }
        } catch (ex: Exception) {
            _errorState.value = ex.message
        }
    }
}