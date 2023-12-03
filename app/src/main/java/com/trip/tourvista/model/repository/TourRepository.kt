package com.trip.tourvista.model.repository

import com.trip.tourvista.model.api.TourApi
import com.trip.tourvista.model.response.TourResponse
import com.trip.tourvista.model.response.TourResponseWrapper
import retrofit2.Call
import retrofit2.Response
import kotlin.math.min

class TourRepository(private val apiService: TourApi) {

    suspend fun getTours (): Response<TourResponseWrapper> {
        return apiService.getOffers()
    }

    suspend fun getAllTours (page:Int, limit:Int): Response<TourResponseWrapper> {
        return apiService.getAllOffers(
            page = page,
            limit = limit
        )
    }

    suspend fun findAllTours (
        name:String,
        location:String,
        min_people:Int,
        max_people:Int,
        start_date:String,
        end_date:String,
        min_price:Int,
        max_price:Int
    ): Response<TourResponseWrapper> {
        return apiService.getAllOffers(
            name = name,
            location = location,
            min_p = min_people,
            max_p = max_people,
            start_date = start_date,
            end_date = end_date,
            min_price = min_price,
            max_price = max_price
        )
    }

    suspend fun getTour (id:Long): Response<TourResponseWrapper> {
        return apiService.getOffer(id)
    }

}