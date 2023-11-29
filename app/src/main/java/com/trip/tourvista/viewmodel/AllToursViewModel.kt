package com.trip.tourvista.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
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
class AllToursViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val tourApi: TourApi
) : ViewModel() {

    private val KEY_SCROLL_POSITION = "scroll_position"
    private val tourList = MutableLiveData<BaseResponse<List<TourResponse>>>()
    private val tourRepository = TourRepository(tourApi)

    private var page = 1
    private var limit = 15
    fun showAll () {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = tourRepository.getAllTours(page, limit)
                if (response.isSuccessful) {
                    tourList.postValue(BaseResponse.Success(response.body()!!.offers))
                    page++
                } else {
                    tourList.postValue(BaseResponse.Error(response.message()))
                }
            } catch (ex: Exception) {
                tourList.postValue(BaseResponse.Error(ex.message))
            }
        }
    }


    fun saveScrollPosition(position: Int) {
        savedStateHandle.set(KEY_SCROLL_POSITION, position)
    }

    fun getScrollPosition(): Int {
        return savedStateHandle.get(KEY_SCROLL_POSITION) ?: 0
    }

    fun getTourList () : LiveData<BaseResponse<List<TourResponse>>> {
        return tourList
    }

}