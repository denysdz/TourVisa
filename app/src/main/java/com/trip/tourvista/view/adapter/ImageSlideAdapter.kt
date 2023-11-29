package com.trip.tourvista.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.trip.tourvista.R


class ImageSlideAdapter(
    private val context: Context,
    private val layout:Int,
    private val tour: List<String>
    ) :
    RecyclerView.Adapter<ImageSlideAdapter.SliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val tourItem = tour[position]
        Glide.with(context)
            .load(tourItem)
            .apply(
                RequestOptions()
                    .timeout(5000)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.tmpl)
                    .error(R.drawable.tmpl)
            )
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = tour.size

    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.img)
    }

}