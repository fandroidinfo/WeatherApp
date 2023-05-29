package info.fandroid.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName


data class Rain (

  @SerializedName("1h" ) var oneHour : Double? = null

)