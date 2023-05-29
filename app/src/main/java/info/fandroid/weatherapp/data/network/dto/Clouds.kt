package info.fandroid.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName


data class Clouds (

  @SerializedName("all" ) var all : Int? = null

)