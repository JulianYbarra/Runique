package com.junka.analytics.presentation

sealed interface AnalyticsAction {
    data object OnBackClick : AnalyticsAction
}