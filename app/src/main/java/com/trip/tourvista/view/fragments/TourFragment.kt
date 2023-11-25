package com.trip.tourvista.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.trip.tourvista.R
import com.trip.tourvista.databinding.FragmentTourBinding
import com.trip.tourvista.model.response.BaseResponse
import com.trip.tourvista.model.response.TourResponse
import com.trip.tourvista.view.adapter.ImageSlideAdapter
import com.trip.tourvista.view.adapter.ListAdapter
import com.trip.tourvista.viewmodel.TourViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Array


@AndroidEntryPoint
class TourFragment : Fragment() {

    private lateinit var binding:FragmentTourBinding
    private val viewBinding get() = binding

    private lateinit var imageSlideAdapter : ImageSlideAdapter
    private lateinit var dots: kotlin.Array<ImageView?>

    private var moreData = ""

    private val viewModel: TourViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTourBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getLong("id").let {
            viewModel.loadTour(it!!)
        }
        updateUi()
        viewBinding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        viewBinding.shimmerContainer.get(0).findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            findNavController().popBackStack()
        }
        viewBinding.btnBookNow.setOnClickListener {
            if (!moreData.equals("")) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(moreData))
                requireActivity().startActivity(intent)
            }
        }
    }

    private fun updateUi () {
        viewModel.getTour().observe(viewLifecycleOwner, Observer {
            when (it) {
                is BaseResponse.Error -> {}
                is BaseResponse.Loading -> {}
                is BaseResponse.Success -> {
                    setValues(it.data?.get(0)!!)
                    initSlider(it.data?.get(0)!!.links)
                    hidePreloader()
                }
            }
        })
    }

    private fun initSlider (imgs:List<String>) {
        imageSlideAdapter = ImageSlideAdapter(requireContext(), R.layout.slider_img, imgs)
        viewBinding.tourSliderImage.adapter = imageSlideAdapter
        initDots(imgs.size)
    }

    private fun setValues (tour:TourResponse) {
        moreData = tour.offerSource
        viewBinding.fieldName.text = tour.offerName
        viewBinding.fieldLocation.text = tour.location
        viewBinding.fieldTransport.text = tour.transportInfo
        viewBinding.fieldFood.text = tour.foodInfo
        viewBinding.fieldPeople.text = tour.peopleCount.toString()
        viewBinding.fieldDescription.text = tour.description
        viewBinding.fieldDate.text = "${getShortDate(tour.startDate)} - ${getShortDate(tour.endDate)}"
        viewBinding.fieldPrice.text = "${tour.price} UAN"
    }

    private fun hidePreloader () {
        viewBinding.shimmerSliderImage.stopShimmer()
        viewBinding.shimmerContainer.stopShimmer()
        viewBinding.shimmerSliderImage.visibility = View.GONE
        viewBinding.shimmerContainer.visibility = View.GONE
        viewBinding.mainContainer.visibility = View.VISIBLE
    }

    private fun getShortDate (date:String) : String {
        return date.substring(0, 10)
    }

    private fun initDots (dotsCount:Int) {
        dots = arrayOfNulls<ImageView>(dotsCount)
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
        viewBinding.tourSliderImage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
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

}