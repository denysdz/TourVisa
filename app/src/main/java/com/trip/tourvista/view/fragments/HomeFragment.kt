package com.trip.tourvista.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.trip.tourvista.R
import com.trip.tourvista.databinding.FragmentHomeBinding
import com.trip.tourvista.model.response.BaseResponse
import com.trip.tourvista.model.response.TourResponse
import com.trip.tourvista.view.adapter.AdapterListener
import com.trip.tourvista.view.adapter.ItemDecoration
import com.trip.tourvista.view.adapter.ListAdapter
import com.trip.tourvista.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), AdapterListener {

    private val viewModel:HomeViewModel by viewModels()

    private lateinit var binding: FragmentHomeBinding
    private lateinit var sliderAdapter: ListAdapter
    private lateinit var listAdapter: ListAdapter

    private var dotsCount: Int = 3
    private var dots: Array<ImageView?> = arrayOfNulls<ImageView>(3)
    private val viewBinding get() = binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadTours()
        val spaceHeightInPixels = resources.getDimensionPixelSize(R.dimen.top_space)
        val itemDecoration = ItemDecoration(spaceHeightInPixels)
        binding.tourList.addItemDecoration(itemDecoration)
        updateUi()
        viewBinding.btnShowAll.setOnClickListener {
            findNavController().navigate(R.id.allToursFragment)
        }
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
        val slider = list?.subList(0, 3)
        val bottomList = list?.subList(3, list.size-1)
        sliderAdapter = ListAdapter(requireContext(), R.layout.slider_item, slider?.toMutableList()!!, this)
        listAdapter = ListAdapter(requireContext(), R.layout.list_item, bottomList?.toMutableList()!!, this)
        binding.shimmerLayoutList.stopShimmer()
        binding.shimmerLayoutSlider.stopShimmer()
        binding.shimmerLayoutList.visibility = View.GONE
        binding.shimmerLayoutSlider.visibility = View.GONE
        binding.tourSlider.visibility = View.VISIBLE
        binding.tourList.visibility = View.VISIBLE
        viewBinding.tourSlider.adapter = sliderAdapter
        viewBinding.tourList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewBinding.tourList.adapter = listAdapter
        initDots()
    }

    private fun initDots () {
        viewBinding.dots.removeAllViews()
        for (i in 0 until dotsCount) {
            dots[i] = ImageView(requireContext())
            dots[i]?.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.non_active_dot))
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            viewBinding.dots.addView(dots[i], params)
        }
        dots[0]?.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.active_dot));
        viewBinding.tourSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                for (i in 0 until dotsCount) {
                    dots[i]?.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.non_active_dot))
                }
                dots[position]?.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.active_dot))
            }

            override fun onPageScrollStateChanged(state: Int) {}

        })
    }


    override fun onClick(id: Long) {
        val bundle = Bundle()
        bundle.putLong("id", id)
        findNavController().navigate(R.id.tourFragment, bundle)
    }

}