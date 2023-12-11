package com.mikko.todo.login

sealed class LoginFormState

data class FailedLoginFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null
) : LoginFormState()

data class SuccessfulLoginFormState(
    val isDataValid: Boolean = false
) : LoginFormState()