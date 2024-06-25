package com.junka.core.connectivity.data.messaging

import com.junka.core.connectivity.domain.messaging.MessagingAction

fun MessagingActionDto.toMessagingAction(): MessagingAction {
    return when (this) {
        MessagingActionDto.StartOrResume -> MessagingAction.StartOrResume
        MessagingActionDto.Pause -> MessagingAction.Pause
        MessagingActionDto.Finish -> MessagingAction.Finish
        MessagingActionDto.Trackable -> MessagingAction.Trackable
        MessagingActionDto.UnTrackable -> MessagingAction.UnTrackable
        MessagingActionDto.ConnectionRequest -> MessagingAction.ConnectionRequest
        is MessagingActionDto.HearRateUpdate -> MessagingAction.HearRateUpdate(heartRate)
        is MessagingActionDto.DistanceUpdate -> MessagingAction.DistanceUpdate(distance)
        is MessagingActionDto.TimeUpdate -> MessagingAction.TimeUpdate(duration)
    }
}

fun MessagingAction.toMessagingActionDto(): MessagingActionDto {
    return when (this) {
        MessagingAction.StartOrResume -> MessagingActionDto.StartOrResume
        MessagingAction.Pause -> MessagingActionDto.Pause
        MessagingAction.Finish -> MessagingActionDto.Finish
        MessagingAction.Trackable -> MessagingActionDto.Trackable
        MessagingAction.UnTrackable -> MessagingActionDto.UnTrackable
        MessagingAction.ConnectionRequest -> MessagingActionDto.ConnectionRequest
        is MessagingAction.HearRateUpdate -> MessagingActionDto.HearRateUpdate(heartRate)
        is MessagingAction.DistanceUpdate -> MessagingActionDto.DistanceUpdate(distance)
        is MessagingAction.TimeUpdate -> MessagingActionDto.TimeUpdate(duration)
    }
}