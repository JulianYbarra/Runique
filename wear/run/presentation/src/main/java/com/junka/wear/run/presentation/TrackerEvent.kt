package com.junka.wear.run.presentation

import com.junka.core.presentation.ui.UiText

sealed interface TrackerEvent {
    data object RunFinished : TrackerEvent
    data class Error(val message: UiText) : TrackerEvent
}