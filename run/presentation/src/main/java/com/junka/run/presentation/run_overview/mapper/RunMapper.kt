package com.junka.run.presentation.run_overview.mapper

import com.junka.core.domain.run.Run
import com.junka.core.presentation.ui.formatted
import com.junka.core.presentation.ui.toFormattedHeartRate
import com.junka.core.presentation.ui.toFormattedKm
import com.junka.core.presentation.ui.toFormattedKmH
import com.junka.core.presentation.ui.toFormattedMeters
import com.junka.core.presentation.ui.toFormattedPace
import com.junka.run.presentation.run_overview.model.RunUi
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun Run.toRunUi(): RunUi {

    val dateTimeInLocalTime = dateTimeUtc
        .withZoneSameInstant(ZoneId.systemDefault())

    val formattedDateTime = DateTimeFormatter
        .ofPattern("MMM dd, yyyy - hh:mma")
        .format(dateTimeInLocalTime)

    val distanceKm = distanceMeters / 1000.0

    return RunUi(
        id = id!!,
        duration = duration.formatted(),
        dateTime = formattedDateTime,
        distance = distanceKm.toFormattedKm(),
        avgSpeed =  avgSpeedKmH.toFormattedKmH(),
        maxSpeed = maxSpeedKmH.toFormattedKmH(),
        pace = duration.toFormattedPace(distanceKm),
        totalElevation = totalElevationMeters.toFormattedMeters(),
        mapPictureUrl = mapPictureUrl,
        maxHeartRate = maxHeartRate.toFormattedHeartRate(),
        avgHeartRate = avgHeartRate.toFormattedHeartRate()
    )
}