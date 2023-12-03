package com.trip.tourvista.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.trip.tourvista.R
import com.trip.tourvista.databinding.FragmentSearchBinding
import com.trip.tourvista.model.response.BaseResponse
import com.trip.tourvista.model.response.TourResponse
import com.trip.tourvista.view.TourActivity
import com.trip.tourvista.view.adapter.AdapterListener
import com.trip.tourvista.view.adapter.ItemDecoration
import com.trip.tourvista.view.adapter.ListAdapter
import com.trip.tourvista.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class SearchFragment : Fragment(), AdapterListener {

    private lateinit var binding:FragmentSearchBinding
    private val viewBinding get() = binding

    private lateinit var listAdapter: ListAdapter

    private val viewModel:SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        val spaceHeightInPixels = resources.getDimensionPixelSize(R.dimen.top_space)
        val itemDecoration = ItemDecoration(spaceHeightInPixels)
        binding.tourList.addItemDecoration(itemDecoration)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.dateTo.setOnClickListener {
            selectDate()
        }
        viewBinding.dateFrom.setOnClickListener {
            selectDate()
        }
        viewBinding.btnSearchNow.setOnClickListener {
            onSearch()
        }
        viewBinding.searchBtn.setOnClickListener {
            viewBinding.emptyContainer.visibility = View.GONE
            viewBinding.tourList.visibility = View.GONE
            binding.shimmerLayoutList.startShimmer()
            binding.shimmerLayoutList.visibility = View.VISIBLE
            viewBinding.searchBtn.visibility = View.GONE
            viewBinding.resultContainer.visibility = View.GONE
            viewBinding.searchContainer.visibility = View.VISIBLE
        }
        updateUi()
    }

    private fun updateUi () {
        viewModel.getTourList().observe(viewLifecycleOwner, Observer {
            when (it) {
                is BaseResponse.Error -> {}
                is BaseResponse.Loading -> {}
                is BaseResponse.Success -> {
                    loadData(it.data)
                }
            }
        })
    }

    private fun loadData (list: List<TourResponse>?) {
        binding.shimmerLayoutList.stopShimmer()
        binding.shimmerLayoutList.visibility = View.GONE
        if (list?.size!! > 0) {
            listAdapter =
                ListAdapter(requireContext(), R.layout.list_item, list?.toMutableList()!!, this)
            binding.tourList.visibility = View.VISIBLE
            viewBinding.tourList.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            viewBinding.tourList.adapter = listAdapter
        } else {
            viewBinding.tourList.visibility = View.GONE
            viewBinding.emptyContainer.visibility = View.VISIBLE
        }
    }

    private fun onSearch () {
        viewModel.findTours(
            name = viewBinding.editTour.text.toString(),
            location = viewBinding.editLocation.text.toString(),
            min_people = viewBinding.peopleRange.values[0].toInt(),
            max_people = viewBinding.peopleRange.values[1].toInt(),
            start_date = viewBinding.dateFrom.text.toString(),
            end_date = viewBinding.dateTo.text.toString(),
            min_price = viewBinding.priceRange.values[0].toInt(),
            max_price = viewBinding.priceRange.values[1].toInt()
        )
        viewBinding.searchContainer.visibility = View.GONE
        viewBinding.searchBtn.visibility = View.VISIBLE
        viewBinding.resultContainer.visibility = View.VISIBLE
    }

    private fun selectDate () {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        builder.setTitleText("Select a date range")
        val datePicker: MaterialDatePicker<Pair<Long, Long>> = builder.build()
        datePicker.addOnPositiveButtonClickListener { selection ->
            val startDate = selection.first
            val endDate = selection.second
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val startDateString = sdf.format(Date(startDate))
            val endDateString = sdf.format(Date(endDate))
            viewBinding.dateFrom.text = startDateString.toString()
            viewBinding.dateTo.text = endDateString.toString()
        }
        datePicker.show(requireActivity().supportFragmentManager, "DATE_PICKER")
    }

    override fun onClick(id: Long) {
        val tour = Intent(requireContext(), TourActivity::class.java)
        tour.putExtra("id", id)
        startActivity(tour)
    }

}