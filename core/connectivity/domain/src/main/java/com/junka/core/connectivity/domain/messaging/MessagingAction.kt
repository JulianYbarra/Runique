package com.junka.core.connectivity.domain.messaging

import kotlin.time.Duration

sealed interface MessagingAction {
    data object StartOrResume : MessagingAction
    data object Pause : MessagingAction
    data object Finish : MessagingAction
    data object Trackable : MessagingAction
    data object UnTrackable : MessagingAction
    data object ConnectionRequest : MessagingAction
    data class HearRateUpdate(val heartRate: Int) : MessagingAction
    data class DistanceUpdate(val distance: Int) : MessagingAction
    data class TimeUpdate(val duration: Duration) : MessagingAction
}