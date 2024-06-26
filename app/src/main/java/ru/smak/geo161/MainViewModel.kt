package ru.smak.geo161

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.smak.geo161.locating.Locator

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val fusedLocationClient = LocationServices
        .getFusedLocationProviderClient(app.applicationContext)

    val location: StateFlow<Location?>
        get() = Locator.location

    fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                getApplication<Application>().applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getApplication<Application>().applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnCompleteListener {
            viewModelScope.launch {
                fusedLocationClient.requestLocationUpdates(
                    Locator.locationRequest,
                    Locator,
                    Looper.getMainLooper()
                )
            }
        }

    }

}