package com.trip.tourvista.model.db

import androidx.lifecycle.LiveData
import com.trip.tourvista.model.response.TourResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class TourLocalRepository @Inject constructor(private val tourDao: TourDao) {
    fun insert(tour: TourResponse) {
        tourDao.insert(tour)
    }

    fun getAllTours(): Flow<List<TourResponse>> {
        return tourDao.getAllTours()
    }

    fun getTourById(tourId: Long): Flow<TourResponse?> {
        return tourDao.getTourById(tourId)
    }

    fun delete(tour: TourResponse) {
        tourDao.delete(tour)
    }

    fun update(tour: TourResponse) {
        tourDao.update(tour)
    }
}