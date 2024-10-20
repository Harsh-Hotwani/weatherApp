package com.example.weatherapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.weatherapplication.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import weatherapp
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        fetchWeatherData("indore")
        searchCity()
    }

    private fun searchCity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    private fun fetchWeatherData(cityname:String) {
        val Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)

        val response = Retrofit.getWeather(cityname,"94dd04c9c1ac038d2ab02d0aa027385f","metric")
        response.enqueue(object : Callback<weatherapp>{
            override fun onResponse(p0: Call<weatherapp>, p1: Response<weatherapp>) {
                val responseBody = p1.body()
                if(p1.isSuccessful && responseBody != null){
                    val temp = responseBody.main.temp.toString()
                    val city = responseBody.name.toString()
                    val humidity = responseBody.main.humidity
                    val windspeed = responseBody.wind.speed
                    //val pressure = responseBody.main.pressure
                    val sunrise = responseBody.sys.sunrise
                    val sunset = responseBody.sys.sunset
                    val sealevel = responseBody.main.sea_level
                    val condition = responseBody.weather.firstOrNull()?.main ?: "unknown"
                    val maxTemp = responseBody.main.temp_max
                    val minTemp = responseBody.main.temp_min
                    //Log.d("TAG", "onResponse: $temp and $city")

                    binding.temperature.text="$temp°C"
                    binding.condition.text=condition
                    binding.wind.text="$windspeed m/s"
                    binding.humidity.text="$humidity % RH"
                    binding.sea.text="$sealevel hPa"
                    binding.max.text="Max temp: $maxTemp°C"
                    binding.min.text="Min temp: $minTemp°C"
                    binding.sunrise.text="$sunrise"
                    binding.sunset.text="$sunset"
                    binding.textView8.text=condition
                    binding.day.text=dayName(System.currentTimeMillis())
                        binding.date.text=date()
                        binding.cityname.text="$cityname"

                    changeimage(condition)

                }
            }

            override fun onFailure(p0: Call<weatherapp>, p1: Throwable) {
                TODO("Not yet implemented")
            }

        })


    }

    private fun changeimage(condition: String) {
        when(condition){
            "Clear Sky","Sunny","Clear"->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView3.setAnimation(R.raw.sun)

            }
            "Partly clouds","Clouds","Overcast","Mist","Foggy"->{
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView3.setAnimation(R.raw.cloud)

            }
            "Light Rain","Heavy Rain","Rain","Shower Rain","Drizzle"->{
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView3.setAnimation(R.raw.rain)

            }
            "Light Snow","Heavy Snow","Snow","Shower Snow","Blizzard"->{
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView3.setAnimation(R.raw.snow)
            }
            else->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView3.setAnimation(R.raw.sun)

            }
        }
        binding.lottieAnimationView3.playAnimation()
    }

    private fun date(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        return sdf.format(Date())

    }

    fun dayName(timeStamp: Long):String{
        val sdf = SimpleDateFormat("EEEE")
        return sdf.format(Date())
    }
}