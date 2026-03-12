package com.iberdrola.practicas2026.davidcv.data.remote.retrofit

import com.iberdrola.practicas2026.davidcv.data.local.entity.BillEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("Bills")
    suspend fun getBills(): Response<List<BillEntity>>

    @GET("Bills/{id}")
    suspend fun getBillById(@Path("id") id: Int): Response<BillEntity>

    @POST("Bills")
    suspend fun createBill(@Body bill: BillEntity): Response<BillEntity>

    @PUT("Bills/{id}")
    suspend fun updateBill(@Path("id") id: Int, @Body bill: BillEntity): Response<BillEntity>

    @DELETE("Bills/{id}")
    suspend fun deleteBill(@Path("id") id: Int): Response<Unit>
}
