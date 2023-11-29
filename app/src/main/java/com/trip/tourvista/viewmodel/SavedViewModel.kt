package com.trip.tourvista.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trip.tourvista.model.db.TourLocalRepository
import com.trip.tourvista.model.response.BaseResponse
import com.trip.tourvista.model.response.TourResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor (
    private val localRepository: TourLocalRepository
) : ViewModel() {

    private var tourList = MutableStateFlow<List<TourResponse>>(emptyList())

    fun showAll () {
        viewModelScope.launch(Dispatchers.IO) {
            localRepository.getAllTours().collect { tours ->
                tourList.value = tours
            }
        }
    }

    fun getTours(): StateFlow<List<TourResponse>> {
        return tourList
    }

}