package com.junka.run.location

import android.location.Location
import com.junka.core.domain.location.LocationWithAltitude

fun Location.toLocationWithAltitude() : LocationWithAltitude {
    return LocationWithAltitude(
        location = com.junka.core.domain.location.Location(
            lat = latitude,
            long = longitude
        ),
        altitude = altitude
    )
}