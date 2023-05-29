package info.fandroid.weatherapp.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import info.fandroid.weatherapp.R
import info.fandroid.weatherapp.data.WeatherType
import info.fandroid.weatherapp.data.network.dto.WeatherResponse
import kotlin.math.roundToInt

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val weatherUiState = viewModel.weatherUiState
    val searchWidgetState = viewModel.searchWidgetState
    val searchTextState = viewModel.searchTextState

    Scaffold(topBar = {
        SearchAppBar(searchWidgetState = searchWidgetState,
            searchTextState = searchTextState,
            onTextChange = {
                viewModel.updateSearchTextState(newValue = it)
            },
            onCloseClicked = {
                viewModel.updateSearchWidgetState(newValue = SearchWidgetState.CLOSED)
            },
            onSearchClicked = {
                viewModel.getWeather(it.trimEnd())
            },
            onSearchTriggered = {
                viewModel.updateSearchWidgetState(newValue = SearchWidgetState.OPENED)
            })
    },

        content = { paddingValues ->

            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.background),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (weatherUiState) {
                        is WeatherUiState.Success -> {
                            val weather = (weatherUiState).weather
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = weather.name,
                                fontSize = MaterialTheme.typography.h4.fontSize,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = weather.weather.first().description,
                                fontSize = MaterialTheme.typography.h5.fontSize,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                WeatherCard(
                                    icon = painterResource(R.drawable.ic_temperature),
                                    text = formatToInt(
                                        weather.main.temp,
                                        R.string.temperature_degrees
                                    )
                                )
                                WeatherCard(
                                    icon = painterResource(R.drawable.ic_humidity),
                                    text = "${weather.main.humidity}%"
                                )
                                WeatherCard(
                                    icon = painterResource(R.drawable.icon_wind),
                                    text = formatToInt(weather.wind.speed, R.string.wind_speed)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Image(
                                painter = painterResource(id = weather.icon()),
                                contentDescription = "Icon",
                                modifier = Modifier.height(256.dp),
                                contentScale = ContentScale.Inside,
                            )
                        }
                        is WeatherUiState.Error -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.icon_snowman),
                                    contentDescription = "Icon",
                                    modifier = Modifier.height(64.dp),
                                    contentScale = ContentScale.Inside,
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = weatherUiState.errorMessage,
                                    fontSize = MaterialTheme.typography.h4.fontSize,
                                    color = Color.White
                                )

                            }

                        }
                        WeatherUiState.Loading -> {
                            CircularProgressIndicator()
                        }
                        else -> {}
                    }
                }
            }
        })
}

@Composable
private fun formatToInt(value: Double, res: Int): String {
    return stringResource(res, value.roundToInt())
}

@DrawableRes
private fun WeatherResponse.icon(): Int {
    val weather = weather.first().main
    return when {
        weather.contains(
            WeatherType.CLOUDS.description, ignoreCase = true
        ) -> R.drawable.ic_weather_clouds
        weather.contains(
            WeatherType.RAIN.description, ignoreCase = true
        ) -> R.drawable.ic_weather_rain
        weather.contains(
            WeatherType.DRIZZLE.description, ignoreCase = true
        ) -> R.drawable.ic_weather_drizzle
        weather.contains(
            WeatherType.THUNDERSTORM.description, ignoreCase = true
        ) -> R.drawable.ic_weather_thunderstorm
        weather.contains(
            WeatherType.SNOW.description, ignoreCase = true
        ) -> R.drawable.ic_weather_snow
        weather.contains(
            WeatherType.TORNADO.description, ignoreCase = true
        ) -> R.drawable.ic_weather_tornado
        weather.contains(
            WeatherType.SQUALL.description, ignoreCase = true
        ) -> R.drawable.ic_weather_squall
        weather.contains(
            WeatherType.SAND.description, ignoreCase = true
        ) -> R.drawable.ic_weather_sandstorm
        weather.contains(
            WeatherType.CLEAR.description, ignoreCase = true
        ) -> R.drawable.ic_weather_clear
        else -> R.drawable.ic_weather_fog
    }
}
