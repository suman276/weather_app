package com.suman.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*


class SplashScreen : AppCompatActivity() {
    lateinit var mfusedlocation: FusedLocationProviderClient
    private var myrequestcode=1010
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
          mfusedlocation=LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()// 1.


    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if(CheckPermission()){
             if(LocationEnable()){
                 mfusedlocation.lastLocation.addOnCompleteListener{
                     task->
                     var location:Location?=task.result
                     if(location==null){
                         NewLocation()
                     }
                     else{
                         Handler(Looper.getMainLooper()).postDelayed({
                             var intent = Intent(this, MainActivity::class.java)
                             intent.putExtra("lat", location.latitude.toString())
                             intent.putExtra("long", location.longitude.toString())
                             startActivity(intent)
                             finish()// add finish so when from 2nd activity the back is presed splash screen not come
                         },1200)
                     }
                 }
             }else{
                 Toast.makeText(this, "Please turn on location", Toast.LENGTH_SHORT).show()
             }
        }else(
                RequestPermission()
        )
    }

    @SuppressLint("MissingPermission")
    private fun NewLocation() {// if phone play service has been started again the cached data was deleted
        var locationRequest=LocationRequest()
        locationRequest.priority=LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval=0
        locationRequest.fastestInterval=0
        locationRequest.numUpdates=1 // all the above this puts a value in that location address
        mfusedlocation=LocationServices.getFusedLocationProviderClient(this)
        mfusedlocation.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }
    private val locationCallback=object:LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
           var lastLocation:Location=p0.lastLocation
        }
    }
    private fun LocationEnable(): Boolean {// if location isn't turned then get it checked
         var locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun RequestPermission() {// if no permission ask for permission
       ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
       android.Manifest.permission.ACCESS_COARSE_LOCATION),myrequestcode)
    }

    private fun CheckPermission(): Boolean {// lets check if permission is granted
        if(
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED
        )
        {
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==myrequestcode){// the request code i sent is same tht is requested
              if(grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                  getLastLocation()
              }
        }
    }
}