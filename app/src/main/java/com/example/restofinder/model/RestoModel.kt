package com.example.restofinder.model

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng

data class RestoModel(
    val foto: Bitmap? = null,
    val nama: String,
    val alamat: String?,
    val rating: Double?,
    val jamBuka: Boolean?,
    val latLng: LatLng,
    val jarak: Float
)