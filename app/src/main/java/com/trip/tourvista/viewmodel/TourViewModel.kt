package com.trip.tourvista.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trip.tourvista.model.api.TourApi
import com.trip.tourvista.model.db.TourLocalRepository
import com.trip.tourvista.model.repository.TourRepository
import com.trip.tourvista.model.response.TourResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.Flow
import javax.inject.Inject

@HiltViewModel
class TourViewModel @Inject constructor(
    private val tourApi: TourApi,
    private val localRepository: TourLocalRepository,
) : ViewModel() {

    private var id:Long = 0
    private val tourRepository = TourRepository(tourApi)
    private val tour = MutableLiveData<TourResponse>()
    private var _tourSaved = MutableStateFlow<Boolean>(false)

    fun loadTour (id:Long) {
        this.id = id
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = tourRepository.getTour(id)
                if (response.isSuccessful) {
                    tour.postValue(response.body()!!.offers[0])
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        isSaved()
    }

    fun isSaved () {
        viewModelScope.launch(Dispatchers.IO) {
            localRepository.getTourById(id)
                .collect { tourResponse ->
                    if (tourResponse == null) {
                        _tourSaved.emit(false)
                    } else {
                        _tourSaved.emit(true)
                    }
                }
        }
    }

    fun save () {
        viewModelScope.launch(Dispatchers.IO) {
            if (!_tourSaved.value!!) {
                localRepository.insert(
                    tour = tour.value!!
                )
            } else {
                localRepository.delete(
                    tour = tour.value!!
                )
            }
            isSaved()
        }
    }

    fun getTour () : LiveData<TourResponse> {
        return tour
    }

    fun isTourSaved () : StateFlow<Boolean> {
        return _tourSaved
    }


}