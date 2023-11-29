package com.trip.tourvista.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.trip.tourvista.model.db.converter.ListStringConverter
import com.trip.tourvista.model.response.TourResponse

@Database(entities = [TourResponse::class], version = 1, exportSchema = false)
@TypeConverters(ListStringConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tourDao(): TourDao
}