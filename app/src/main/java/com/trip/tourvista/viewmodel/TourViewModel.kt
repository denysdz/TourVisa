package com.trip.tourvista.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trip.tourvista.model.api.TourApi
import com.trip.tourvista.model.repository.TourRepository
import com.trip.tourvista.model.response.BaseResponse
import com.trip.tourvista.model.response.TourResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TourViewModel @Inject constructor(
    private val tourApi: TourApi
) : ViewModel() {

    private val tourRepository = TourRepository(tourApi)
    private val tour = MutableLiveData<BaseResponse<List<TourResponse>>>()

    fun loadTour (id:Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = tourRepository.getTour(id)
                if (response.isSuccessful) {
                    tour.postValue(BaseResponse.Success(response.body()!!.offers))
                } else {
                    tour.postValue(BaseResponse.Error(response.message()))
                }
            } catch (ex: Exception) {
                tour.postValue(BaseResponse.Error(ex.message))
            }
        }
    }

    fun getTour () : LiveData<BaseResponse<List<TourResponse>>> {
        return tour
    }


}