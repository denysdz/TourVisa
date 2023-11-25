package com.trip.tourvista.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trip.tourvista.model.api.TourApi
import com.trip.tourvista.model.repository.TourRepository
import com.trip.tourvista.model.response.BaseResponse
import com.trip.tourvista.model.response.TourResponse
import com.trip.tourvista.model.response.TourResponseWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val tourApi: TourApi
) : ViewModel() {

    private val tourList = MutableLiveData<BaseResponse<List<TourResponse>>>()
    private val tourRepository = TourRepository(tourApi)

    fun loadTours () {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = tourRepository.getTours()
                if (response.isSuccessful) {
                    tourList.postValue(BaseResponse.Success(response.body()!!.offers))
                } else {
                    tourList.postValue(BaseResponse.Error(response.message()))
                }
            } catch (ex: Exception) {
                tourList.postValue(BaseResponse.Error(ex.message))
            }
        }
    }

    fun getTourList () : LiveData<BaseResponse<List<TourResponse>>> {
        return tourList
    }

}