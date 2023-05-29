package info.fandroid.weatherapp.ui

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import info.fandroid.weatherapp.data.WeatherRepository
import info.fandroid.weatherapp.data.network.dto.WeatherResponse
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    var weatherUiState: WeatherUiState by mutableStateOf(WeatherUiState.Loading)
        private set

    var searchWidgetState by mutableStateOf(SearchWidgetState.CLOSED)
        private set

    var searchTextState by mutableStateOf("")
        private set

    init {
        getWeather("Kyiv")
    }

    fun getWeather(city: String) {
        viewModelScope.launch {
            try {
                weatherUiState = WeatherUiState.Loading
                val response = weatherRepository.getWeather(city).first()
                weatherUiState = WeatherUiState.Success(response)
            } catch (e: HttpException) {
                weatherUiState = if (e.code() == 404) {
                    WeatherUiState.Error("City not found")
                } else {
                    WeatherUiState.Error("HTTP Error: ${e.code()}")
                }
                Log.d("WeatherViewModel", e.message.toString())
            } catch (e: IOException) {
                weatherUiState = WeatherUiState.Error("Network Error")
                Log.d("WeatherViewModel", e.message.toString())
            } catch (e: Exception) {
                weatherUiState = WeatherUiState.Error("Error occurred: ${e.message}")
                Log.d("WeatherViewModel", e.message.toString())
            }
        }
    }

    fun updateSearchWidgetState(newValue: SearchWidgetState) {
        searchWidgetState = newValue
    }

    fun updateSearchTextState(newValue: String) {
        searchTextState = newValue
    }


}

sealed class WeatherUiState {
    data class Success(val weather: WeatherResponse) : WeatherUiState()
    data class Error(val errorMessage: String) : WeatherUiState()
    object Loading : WeatherUiState()
}

enum class SearchWidgetState {
    OPENED,
    CLOSED
}