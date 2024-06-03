package com.junka.analytics.domain

interface AnalyticsRepository {

    suspend fun getAnalyticsValues() : AnalyticsValues
}