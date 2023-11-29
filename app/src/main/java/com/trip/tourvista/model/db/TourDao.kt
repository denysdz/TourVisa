package com.trip.tourvista.model.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.trip.tourvista.model.response.TourResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Dao
interface TourDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: TourResponse)

    @Query("SELECT * FROM tour_responses")
    fun getAllTours(): Flow<List<TourResponse>>

    @Query("SELECT * FROM tour_responses WHERE offerId = :id")
    fun getTourById(id: Long): Flow<TourResponse?>

    @Delete
    fun delete(note: TourResponse)

    @Update
    fun update(note: TourResponse)
}