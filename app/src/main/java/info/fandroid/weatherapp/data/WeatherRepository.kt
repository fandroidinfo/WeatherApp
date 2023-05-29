package info.fandroid.weatherapp.data

import info.fandroid.weatherapp.data.network.Constants
import info.fandroid.weatherapp.data.network.WeatherApi
import info.fandroid.weatherapp.data.network.dto.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepository @Inject constructor (
    private val weatherApi: WeatherApi
) {
    suspend fun getWeather(city: String): Flow<WeatherResponse> = flow {
        val response = weatherApi.getCurrentWeather(city, Constants.API_KEY)
        emit(response)
    }
}