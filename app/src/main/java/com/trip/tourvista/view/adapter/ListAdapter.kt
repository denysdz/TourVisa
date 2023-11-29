package com.trip.tourvista.view.adapter

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.trip.tourvista.R
import com.trip.tourvista.model.response.TourResponse


class ListAdapter(
    private val context: Context,
    private val layout:Int,
    private val tour: MutableList<TourResponse>,
    private val adapterListener: AdapterListener
    ) :
    RecyclerView.Adapter<ListAdapter.TourViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TourViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return TourViewHolder(view)
    }

    override fun onBindViewHolder(holder: TourViewHolder, position: Int) {
        val tourItem = tour[position]
        Glide.with(context)
            .load(tourItem.links[0])
            .apply(
                RequestOptions()
                    .timeout(5000)
                    .placeholder(R.drawable.tmpl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.tmpl) // Error image in case of loading failure
            )
            .into(holder.imageView)
        holder.name.text = tourItem.offerName
        holder.location.text = tourItem.location
        holder.price.text = "${tourItem.price} UAN"
        holder.itemView.setOnClickListener {
            adapterListener.onClick(tourItem.offerId)
        }
    }

    override fun getItemCount(): Int = tour.size

    inner class TourViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.img)
        val name: TextView = itemView.findViewById(R.id.name)
        val location: TextView = itemView.findViewById(R.id.country)
        val price: TextView = itemView.findViewById(R.id.price)
    }

    fun updateData(newItems: List<TourResponse>) {
        tour.addAll(newItems)
        notifyDataSetChanged()
    }

}

class ItemDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position != RecyclerView.NO_POSITION) {
            outRect.top = spaceHeight
            // You can adjust other offsets as needed (left, right, bottom)
        }
    }
}