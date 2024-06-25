package com.junka.core.connectivity.data.messaging

import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
sealed interface MessagingActionDto {

    @Serializable
    data object StartOrResume : MessagingActionDto
    @Serializable
    data object Pause : MessagingActionDto
    @Serializable
    data object Finish : MessagingActionDto
    @Serializable
    data object Trackable : MessagingActionDto
    @Serializable
    data object UnTrackable : MessagingActionDto
    @Serializable
    data object ConnectionRequest : MessagingActionDto
    @Serializable
    data class HearRateUpdate(val heartRate: Int) : MessagingActionDto
    @Serializable
    data class DistanceUpdate(val distance: Int) : MessagingActionDto
    @Serializable
    data class TimeUpdate(val duration: Duration) : MessagingActionDto
}