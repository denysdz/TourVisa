package com.trip.tourvista.view.fragments

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trip.tourvista.R
import com.trip.tourvista.databinding.FragmentAllToursBinding
import com.trip.tourvista.model.response.BaseResponse
import com.trip.tourvista.model.response.TourResponse
import com.trip.tourvista.view.adapter.AdapterListener
import com.trip.tourvista.view.adapter.ItemDecoration
import com.trip.tourvista.view.adapter.ListAdapter
import com.trip.tourvista.viewmodel.AllToursViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllToursFragment : Fragment(), AdapterListener {

    private lateinit var binding: FragmentAllToursBinding
    private val viewModel:AllToursViewModel by viewModels()

    private var listAdapter: ListAdapter? = null
    private var layoutManagerState: Parcelable? = null
    private val viewBinding get() = binding


    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllToursBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onResume() {
        super.onResume()
        if (listAdapter != null) {
            binding.shimmerLayoutList.stopShimmer()
            binding.shimmerLayoutList.visibility = View.GONE
            binding.tourList.visibility = View.VISIBLE
            viewBinding.tourList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            viewBinding.tourList.adapter = listAdapter
            (viewBinding.tourList.layoutManager as LinearLayoutManager).scrollToPosition(viewModel.getScrollPosition())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.showAll()
        val spaceHeightInPixels = resources.getDimensionPixelSize(R.dimen.top_space)
        val itemDecoration = ItemDecoration(spaceHeightInPixels)
        binding.tourList.addItemDecoration(itemDecoration)
        viewBinding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        viewBinding.tourList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                    viewModel.showAll()
                    isLoading = true
                }
            }
        })
        updateUi()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        layoutManagerState = viewBinding.tourList.layoutManager?.onSaveInstanceState()
        outState.putParcelable("list_position", layoutManagerState)
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
        if (listAdapter == null) {
            listAdapter = ListAdapter(requireContext(), R.layout.list_item, list?.toMutableList()!!, this)
            binding.shimmerLayoutList.stopShimmer()
            binding.shimmerLayoutList.visibility = View.GONE
            binding.tourList.visibility = View.VISIBLE
            viewBinding.tourList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            viewBinding.tourList.adapter = listAdapter
        } else {
            listAdapter?.updateData(list?.toMutableList()!!)
        }
        isLoading = false
    }

    override fun onClick(id: Long) {
        val bundle = Bundle()
        bundle.putLong("id", id)
        findNavController().navigate(R.id.tourFragment, bundle)
    }

    override fun onPause() {
        super.onPause()
        if (viewBinding.tourList.layoutManager is LinearLayoutManager) {
            val layoutManager = viewBinding.tourList.layoutManager as LinearLayoutManager
            viewModel.saveScrollPosition(layoutManager.findFirstVisibleItemPosition())
        }
    }

}