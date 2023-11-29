package com.trip.tourvista.model.api

import com.trip.tourvista.model.response.TourResponse
import com.trip.tourvista.model.response.TourResponseWrapper
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TourApi {
    @GET("/offers")
    suspend fun getOffers(): Response<TourResponseWrapper>

    @GET("/offers")
    suspend fun getAllOffers(
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): Response<TourResponseWrapper>


    @GET("/offer/{id}")
    suspend fun getOffer(
        @Path("id") id:Long
    ): Response<TourResponseWrapper>

}