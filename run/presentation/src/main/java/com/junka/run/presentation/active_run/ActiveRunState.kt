package com.junka.run.presentation.active_run

import com.junka.core.domain.location.Location
import com.junka.run.domain.RunData
import kotlin.time.Duration

data class ActiveRunState(
    val elapsedTime: Duration = Duration.ZERO,
    val runData : RunData = RunData(),
    val shouldTrack: Boolean = false,
    val hasStartedRunning: Boolean = false,
    val currentLocation: Location? = null,
    val isRunFinished: Boolean = false,
    val isSavingRun: Boolean = false,
    val showLocationRationale : Boolean = false,
    val showNotificationRationale : Boolean = false
)