package info.fandroid.weatherapp.data

enum class WeatherType(val description: String) {
    CLEAR("Clear"),
    CLOUDS("Clouds"),
    RAIN("Rain"),
    DRIZZLE("Drizzle"),
    THUNDERSTORM("Thunderstorm"),
    SNOW("Snow"),
    MIST("Mist"),
    SMOKE("Smoke"),
    HAZE("Haze"),
    DUST("Dust"),
    FOG("Fog"),
    SAND("Sand"),
    ASH("Ash"),
    SQUALL("Squall"),
    TORNADO("Tornado");

    override fun toString(): String {
        return description
    }
}
