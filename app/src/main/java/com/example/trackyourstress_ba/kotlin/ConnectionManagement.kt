package com.example.trackyourstress_ba.kotlin

import khttp.responses.Response

fun registerUser(user: User): Response{
    val mail = user.email
    val password = user.password
    val password_confirmation = user.password_confirm
    val name = user.username
    val data = "{'data' : { 'type' : 'users', 'attributes' : {'email' : '$mail', 'password' : '$password', 'password_confirmation' : '$password_confirmation', 'name' : '$name}}}"
    val response = khttp.post(url=GlobalVariables.apiEndPoint+ "/api/v1/auth/register", json=data)
    return response
}

fun loginUser(email: String, password: String): Response {
    val data = "{ 'data' : { 'type' : 'users', 'attributes' : {'email' : '$email', 'password' : '$password'}}}"
    val response = khttp.post(url=GlobalVariables.apiEndPoint + "/api/v1/auth/login", json=data)
    return response
}

fun logoutUser(user: User): Response {
    val url = GlobalVariables.apiEndPoint + "/api/v1/auth/logout?token=" //+ TempValues.
    val response = khttp.delete(url=url)
    return response
}

fun resendVerificationLink(email: String): Response {
    val data = "data = { 'data' : { 'type' : 'users', 'attributes' : {'email' : '$email'}}}"
    val response = khttp.post(url=GlobalVariables.apiEndPoint + "/api/v1/auth/verify/resend", json=data)
    return response
}
