package com.trip.tourvista.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.trip.tourvista.R
import com.trip.tourvista.databinding.FragmentHomeBinding
import com.trip.tourvista.databinding.FragmentSavedBinding
import com.trip.tourvista.model.response.BaseResponse
import com.trip.tourvista.model.response.TourResponse
import com.trip.tourvista.view.adapter.AdapterListener
import com.trip.tourvista.view.adapter.ItemDecoration
import com.trip.tourvista.view.adapter.ListAdapter
import com.trip.tourvista.viewmodel.HomeViewModel
import com.trip.tourvista.viewmodel.SavedViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SavedFragment : Fragment(), AdapterListener {

    private val viewModel: SavedViewModel by viewModels()

    private lateinit var binding: FragmentSavedBinding
    private lateinit var listAdapter: ListAdapter
    private val viewBinding get() = binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.showAll()
        val spaceHeightInPixels = resources.getDimensionPixelSize(R.dimen.top_space)
        val itemDecoration = ItemDecoration(spaceHeightInPixels)
        binding.tourList.addItemDecoration(itemDecoration)
        updateUi()
    }

    private fun updateUi () {
        lifecycleScope.launch {
            viewModel.getTours().collect { tour ->
                loadData(tour)
            }
        }
    }

    private fun loadData (list: List<TourResponse>?) {
        binding.shimmerLayoutList.stopShimmer()
        binding.shimmerLayoutList.visibility = View.GONE
        if (list?.size == 0) {
            viewBinding.emptyContainer.visibility = View.VISIBLE
            return
        }
        binding.tourList.visibility = View.VISIBLE
        listAdapter = ListAdapter(requireContext(), R.layout.list_item, list?.toMutableList()!!, this)
        viewBinding.tourList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewBinding.tourList.adapter = listAdapter
    }

    override fun onClick(id: Long) {
        val bundle = Bundle()
        bundle.putLong("id", id)
        findNavController().navigate(R.id.tourFragment, bundle)
    }

}