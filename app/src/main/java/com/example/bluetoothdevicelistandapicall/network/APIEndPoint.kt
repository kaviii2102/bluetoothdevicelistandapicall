package com.example.bluetoothdevicelistandapicall.network

import com.example.bluetoothdevicelistandapicall.data.model.GithubResponseModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiEndPoint {
    @GET("search/repositories")
    suspend fun getAllRepo(@Query("q") q : String) : Response<GithubResponseModel>
}