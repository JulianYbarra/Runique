package com.junka.run.presentation.active_run

import com.junka.core.presentation.ui.UiText

sealed interface ActiveRunEvent {

    data class Error(val error : UiText) : ActiveRunEvent
    data object RunSaved : ActiveRunEvent

}