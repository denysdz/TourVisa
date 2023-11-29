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
class SearchViewModel @Inject constructor(
    private val tourApi: TourApi
) : ViewModel () {

    private val tourList = MutableLiveData<BaseResponse<List<TourResponse>>>()
    private val tourRepository = TourRepository(tourApi)

    fun findTours (
        name:String,
        min_people:Int,
        max_people:Int,
        start_date:String,
        end_date:String,
        min_price:Int,
        max_price:Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = tourRepository.findAllTours(
                    name = name,
                    min_people = min_people,
                    max_people = max_people,
                    start_date = start_date,
                    end_date = end_date,
                    min_price = min_price,
                    max_price = max_price
                )
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