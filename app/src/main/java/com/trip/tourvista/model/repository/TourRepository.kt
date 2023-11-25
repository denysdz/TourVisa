package com.trip.tourvista.model.repository

import com.trip.tourvista.model.api.TourApi
import com.trip.tourvista.model.response.TourResponse
import com.trip.tourvista.model.response.TourResponseWrapper
import retrofit2.Call
import retrofit2.Response

class TourRepository(private val apiService: TourApi) {

    suspend fun getTours (): Response<TourResponseWrapper> {
        return apiService.getOffers()
    }

    suspend fun getTour (id:Long): Response<TourResponseWrapper> {
        return apiService.getOffer(id)
    }

}