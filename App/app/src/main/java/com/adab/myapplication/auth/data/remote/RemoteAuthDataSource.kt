package com.adab.myapplication.auth.data.remote

import com.adab.myapplication.auth.data.TokenHolder
import com.adab.myapplication.auth.data.User
import com.adab.myapplication.core.Api
import com.adab.myapplication.core.Result
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


object RemoteAuthDataSource {
    interface AuthService {
        @Headers("Content-Type: application/json")
        @POST("/api/auth/login")
        suspend fun login(@Body user: User): TokenHolder
    }

    private val authService: AuthService = Api.retrofit.create(AuthService::class.java)

    suspend fun login(user: User): Result<TokenHolder> {
        try {
            return Result.Success(authService.login(user))
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }
}