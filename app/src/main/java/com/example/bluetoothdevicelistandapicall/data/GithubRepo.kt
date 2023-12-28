package com.example.bluetoothdevicelistandapicall.data

import com.example.bluetoothdevicelistandapicall.network.ApiEndPoint
import com.example.bluetoothdevicelistandapicall.network.RetrofitClient

class GithubRepository {
    private val retrofit = RetrofitClient.getRetrofitInstance().create(ApiEndPoint::class.java)

    suspend fun getAllRepository(query : String) = retrofit.getAllRepo(query)
}