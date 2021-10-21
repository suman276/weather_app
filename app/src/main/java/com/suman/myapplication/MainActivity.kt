package com.suman.myapplication

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var lat= intent.getStringExtra("lat")
        var long= intent.getStringExtra("long")
       // Toast.makeText(this, lat+" "+long, Toast.LENGTH_LONG).show()

        getJsonData(lat,long)
    }


private fun setValues(response: JSONObject){
    city.text=response.getString("name")
    var lat= response.getJSONObject("coord").getString("lat")
    var long= response.getJSONObject("coord").getString("lon")
    coordinates.text="${lat} , ${long}"

    weather.text=response.getJSONArray("weather").getJSONObject(0).getString("main")
    var tempr=response.getJSONObject("main").getString("temp")
    tempr= ((((tempr).toFloat()-273.15)).toInt()).toString()
    temp.text= "${ tempr }°C"

    var mintemp=response.getJSONObject("main").getString("temp_min")
    mintemp=((((mintemp).toFloat()-273.15)).toInt()).toString()
    min_temp.text=mintemp+"°C"
    var maxtemp=response.getJSONObject("main").getString("temp_max")
    maxtemp=((ceil((maxtemp).toFloat()-273.15)).toInt()).toString()
    max_temp.text=maxtemp+"°C"

    pressure.text=response.getJSONObject("main").getString("pressure")
    humidity.text=response.getJSONObject("main").getString("humidity")

    speed.text="speed: "+response.getJSONObject("wind").getString("speed")+"km/hr"
    degree.text= "Degree :"+ response.getJSONObject("wind").getString("deg")+ "°"
    //degree.text= "Gust :"+ response.getJSONObject("wind").getString("gust")
}

}

