package com.example.trackyourstress_ba.kotlin

/*

fun registerUser(user: User): Response {
    val mail = user.email
    val password = user.password
    val password_confirmation = user.password_confirm
    val name = user.username
    val data = "{'data' : { 'type' : 'users', 'attributes' : {'email' : '$mail', 'password' : '$password', 'password_confirmation' : '$password_confirmation', 'name' : '$name}}}"
    //val response = khttp.post(url=GlobalVariables.apiEndPoint+ "/api/v1/auth/register", json=data)
    val (request, response, result) = Fuel.post(GlobalVariables.apiEndPoint + "/api/v1/auth/register").jsonBody(data).response()
    return response
}

fun loginUser(email: String, password: String){
    val data = "{ 'data' : { 'type' : 'users', 'attributes' : {'email' : '$email', 'password' : '$password'}}}"
    //val response = khttp.post(url=GlobalVariables.apiEndPoint + "/api/v1/auth/login", json=data)
    //val (request, response, result) = Fuel.post(GlobalVariables.apiEndPoint + "/api/v1/auth/login").body(data).response()
    val jsonObject = JSONObject(data)
    // Volley post request with parameters
    val request = JsonObjectRequest(
        Request.Method.POST,GlobalVariables.apiEndPoint + "/api/v1/auth/login",jsonObject,
        Response.Listener { response ->
            // Process the json

        }, Response.ErrorListener{
            // Error in request
        })
    // Add the volley post request to the request queue
    Volley.newRequestQueue().addToRequestQueue(request)
    return respoolnse
}

fun logoutUser(user: User): Response {
    val url = GlobalVariables.apiEndPoint + "/api/v1/auth/logout?token=" //+ TempValues.
    //val response = khttp.delete(url=url)
    val (request, response, result) = Fuel.delete(url).response()
    return response
}

fun resendVerificationLink(email: String): Response {
    val data = "data = { 'data' : { 'type' : 'users', 'attributes' : {'email' : '$email'}}}"
    //val response = khttp.post(url=GlobalVariables.apiEndPoint + "/api/v1/auth/verify/resend", json=data)
    val (request, response, result) = Fuel.post(GlobalVariables.apiEndPoint + "api/v1/verify/resend").body(data).response()
    return response
}
*/
