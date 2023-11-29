package com.trip.tourvista.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.trip.tourvista.R
import com.trip.tourvista.databinding.ActivityTourBinding
import com.trip.tourvista.model.response.TourResponse
import com.trip.tourvista.view.adapter.ImageSlideAdapter
import com.trip.tourvista.viewmodel.TourViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class TourActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTourBinding
    private val viewBinding get() = binding

    private lateinit var imageSlideAdapter : ImageSlideAdapter
    private lateinit var dots: kotlin.Array<ImageView?>

    private var moreData = ""

    private val viewModel: TourViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTourBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        intent.extras?.getLong("id").let {
            viewModel.loadTour(it!!)
        }
        updateUi()
        viewBinding.btnBack.setOnClickListener {
            finish()
        }
        viewBinding.shimmerContainer.get(0).findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            finish()
        }
        viewBinding.btnSave.setOnClickListener {
            viewModel.save()
        }
        viewBinding.btnBookNow.setOnClickListener {
            if (!moreData.equals("")) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(moreData))
                startActivity(intent)
            }
        }
    }

    private fun updateUi () {
        lifecycleScope.launch {
            viewModel.isTourSaved().collect{  state ->
                when (state) {
                    true -> {
                        viewBinding.btnSave.setImageResource(R.drawable.ic_saved_active)
                    }
                    false -> {
                        viewBinding.btnSave.setImageResource(R.drawable.ic_saved_inactive)
                    }
                }
            }
        }
        viewModel.getTour().observe(this, Observer {
            setValues(it)
            initSlider(it.links)
            hidePreloader()
        })
    }

    private fun initSlider (imgs:List<String>) {
        imageSlideAdapter = ImageSlideAdapter(this, R.layout.slider_img, imgs)
        viewBinding.tourSliderImage.adapter = imageSlideAdapter
        initDots(imgs.size)
    }

    private fun setValues (tour: TourResponse) {
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
            dots[i] = ImageView(this)
            dots[i]?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.non_active_dot))
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            viewBinding.dots.addView(dots[i], params)
        }
        dots[0]?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dot));
        viewBinding.tourSliderImage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                for (i in 0 until dotsCount) {
                    dots[i]?.setImageDrawable(ContextCompat.getDrawable(this@TourActivity, R.drawable.non_active_dot))
                }
                dots[position]?.setImageDrawable(ContextCompat.getDrawable(this@TourActivity, R.drawable.active_dot))
            }

            override fun onPageScrollStateChanged(state: Int) {}

        })
    }

}