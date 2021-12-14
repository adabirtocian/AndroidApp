package com.adab.myapplication.coffees.data.remote

import com.adab.myapplication.coffees.data.Coffee
import com.adab.myapplication.coffees.data.CoffeeWrapper
import com.adab.myapplication.core.Api
import retrofit2.http.*

object CoffeeApi {
    interface Service {
        @GET("/api/coffee")
        suspend fun find(): List<Coffee>

        @Headers("Content-Type: application/json")
        @POST("/api/coffee")
        suspend fun create(@Body coffee: Coffee): Coffee

        @Headers("Content-Type: application/json")
        @PUT("/api/coffee/{id}")
        suspend fun update(@Path("id") coffeeId: String, @Body coffee: Coffee): Coffee
    }

    val service: Service = Api.retrofit.create(Service::class.java)
}