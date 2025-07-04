package com.example.restofinder.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.example.restofinder.model.RestoModel
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Suppress("DEPRECATION")
class RestoRepository(private val context: Context) {

    private val placesClient = Places.createClient(context)
    private val fusedLocation = LocationServices.getFusedLocationProviderClient(context)

    suspend fun getNearbyResto(): List<RestoModel> = withContext(Dispatchers.IO) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return@withContext emptyList()
        }

        val userLoc = fusedLocation.lastLocation.await() ?: return@withContext emptyList()

        val request = FindCurrentPlaceRequest.newInstance(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.RATING,
                Place.Field.OPENING_HOURS,
                Place.Field.PHOTO_METADATAS,
                Place.Field.LAT_LNG
            )
        )

        val response = placesClient.findCurrentPlace(request).await()
        val resultList = mutableListOf<RestoModel>()
        val places = response.placeLikelihoods

        for (pl in places) {
            val p = pl.place
            val loc = p.latLng ?: continue

            val jarak = hitungJarak(
                userLoc.latitude, userLoc.longitude, loc.latitude, loc.longitude
            )

            if (jarak <= 50f) continue

            val foto = try {
                val photoMetadata = p.photoMetadatas?.firstOrNull()
                if (photoMetadata != null) {
                    val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                        .setMaxWidth(600)
                        .setMaxHeight(400)
                        .build()
                    placesClient.fetchPhoto(photoRequest).await().bitmap
                } else null
            } catch (e: Exception) {
                null
            }

            resultList.add(
                RestoModel(
                    nama = p.name ?: "Tidak memiliki nama",
                    alamat = p.address ?: "Alamat tidak tersedia",
                    rating = p.rating ?: 0.0,
                    jamBuka = p.isOpen ?: false,
                    latLng = loc,
                    foto = foto,
                    jarak = jarak
                )
            )
        }

        return@withContext resultList.sortedBy { it.jarak }
    }

    private fun hitungJarak(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val result = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, result)
        return result[0] / 1000f
    }
}