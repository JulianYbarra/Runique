package com.junka.run.network

import com.junka.core.domain.location.Location
import com.junka.core.domain.run.Run
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration.Companion.milliseconds

fun RunDto.toRun(): Run {
    return Run(
        id = id,
        duration = durationMillis.milliseconds,
        dateTimeUtc = Instant.parse(dateTimeUtc)
            .atZone(ZoneId.of("UTC")),
        distanceMeters = distanceMeters,
        location = Location(lat, long),
        maxSpeedKmH = maxSpeedKmh,
        totalElevationMeters = totalElevationMeters,
        mapPictureUrl = mapPictureUrl
    )
}

fun Run.toCreateRunRequest() = CreateRunRequest(
    id = id!!,
    durationMillis = duration.inWholeMilliseconds,
    distanceMeters = distanceMeters,
    lat = location.lat,
    long = location.long,
    avgSpeedKmH = avgSpeedKmH,
    maxSpeedKmH = maxSpeedKmH,
    totalElevationMeters = totalElevationMeters,
    epochMillis = dateTimeUtc.toEpochSecond() * 1000L
)