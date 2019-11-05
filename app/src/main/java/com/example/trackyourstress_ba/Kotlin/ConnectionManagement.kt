package com.example.trackyourstress_ba.Kotlin

fun registerUser(user: User): Int{
    val mail = user.email
    val password = user.password
    val password_confirmation = user.password_confirm
    val name = user.username
    val data = "{'data' : { 'type' : 'users', 'attributes' : {'email' : '$mail', 'password' : '$password', 'password_confirmation' : '$password_confirmation', 'name' : '$name}}}"
    val response = khttp.post(url=GlobalVariables.apiEndPoint+ "/api/v1/auth/register", json=data)
    return response.statusCode
}

fun loginUser(user: User): Int {
    val mail = user.email
    val password = user.password
    val data = "{ 'data' : { 'type' : 'users', 'attributes' : {'email' : '$mail', 'password' : '$password'}}}"
    val response = khttp.post(url=GlobalVariables.apiEndPoint + "/api/v1/auth/login", json=data)
    return response.statusCode
}

fun logoutUser(user: User): Int {
    val url = GlobalVariables.apiEndPoint + "/api/v1/auth/logout?token=" //+ TempValues.
    val response = khttp.delete(url=url)
    return response.statusCode
}

fun resendVerificationLink(email: String): Int {
    val data = "data = { 'data' : { 'type' : 'users', 'attributes' : {'email' : '$email'}}}"
    val response = khttp.post(url=GlobalVariables.apiEndPoint + "/api/v1/auth/verify/resend", json=data)
    return response.statusCode
}
