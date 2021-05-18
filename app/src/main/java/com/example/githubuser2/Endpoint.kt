package com.example.githubuser2

import retrofit2.Call
import retrofit2.http.GET

interface Endpoint {
    @GET("users")
    fun getUsers():Call<List<DataUserGithub>>
}