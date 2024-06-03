package com.junka.analytics.presentation.mapper

import com.junka.analytics.domain.AnalyticsValues
import com.junka.analytics.presentation.AnalyticsDashboardState
import com.junka.core.presentation.ui.formatted
import com.junka.core.presentation.ui.toFormattedKm
import com.junka.core.presentation.ui.toFormattedKmH
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

fun Duration.toFormattedTotalTime(): String {
    val days = toLong(DurationUnit.DAYS)
    val hours = toLong(DurationUnit.HOURS) % 24
    val minutes = toLong(DurationUnit.MINUTES) % 60
    return "${days}d ${hours}h ${minutes}m"
}

fun AnalyticsValues.toAnalyticsDashboardState() = AnalyticsDashboardState(
    totalDistanceRun = (totalDistanceRun / 1000.0).toFormattedKm(),
    totalTimeRun = totalTimeRun.toFormattedTotalTime(),
    fastestEverRun = fastestEverRun.toFormattedKmH(),
    avgDistance = (avgDistancePerRun / 1000.0).toFormattedKm(),
    avgPace = avgPacePerRun.seconds.formatted()
)