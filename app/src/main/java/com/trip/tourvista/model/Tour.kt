package com.trip.tourvista.model

data class Tour (
    val id:Int,
    val name:String,
    val location:String,
    val country:String,
    val price:Int,
    val img:List<String>
)