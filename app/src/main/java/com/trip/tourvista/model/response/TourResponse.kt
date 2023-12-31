package com.trip.tourvista.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


data class TourResponseWrapper(
    @SerializedName("offer")
    val offers: List<TourResponse>
)

@Entity(tableName = "tour_responses")
data class TourResponse(
    @SerializedName("offer_id")
    @PrimaryKey(autoGenerate = true)
    val id:Int,

    @SerializedName("description")
    val description: String,

    @SerializedName("end_date")
    val endDate: String,

    @SerializedName("food_info")
    val foodInfo: String,

    @SerializedName("links")
    val links: List<String>,

    @SerializedName("location")
    val location: String,

    @SerializedName("night_count")
    val nightCount: Int,

    @SerializedName("uniq_id")
    val offerId: Long,

    @SerializedName("offer_name")
    val offerName: String,

    @SerializedName("offer_source")
    val offerSource: String,

    @SerializedName("people_count")
    val peopleCount: Int,

    @SerializedName("price")
    val price: Int,

    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("transport_info")
    val transportInfo: String
)