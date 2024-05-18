package com.junka.auth.domain

interface PatternValidator {
    fun matches(value: String): Boolean
}