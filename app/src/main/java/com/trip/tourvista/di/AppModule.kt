package com.trip.tourvista.di

import android.content.Context
import androidx.room.Room
import com.trip.tourvista.model.db.AppDatabase
import com.trip.tourvista.model.db.TourDao
import com.trip.tourvista.model.db.TourLocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "room_database"
        ).build()
    }
    @Provides
    @Singleton
    fun provideDao(appDatabase: AppDatabase) : TourDao{
        return appDatabase.tourDao()
    }
    @Provides
    @Singleton
    fun provideTaskRepository(tourDao: TourDao): TourLocalRepository {
        return TourLocalRepository(tourDao)
    }
}