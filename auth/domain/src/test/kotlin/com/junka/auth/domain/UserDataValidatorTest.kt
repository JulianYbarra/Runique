package com.junka.auth.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class UserDataValidatorTest {

    private lateinit var userDataValidator: UserDataValidator

    @BeforeEach
    fun setUp() {
        userDataValidator = UserDataValidator(
            patternValidator = object : PatternValidator {
                override fun matches(value: String): Boolean {
                    return true
                }
            }
        )
    }

    @Test
    fun testValidateEmail() {
        val email = "test@test.com"

        val isValid = userDataValidator.isValidEmail(email)

        assertThat(isValid).isTrue()
    }

    @ParameterizedTest
    @CsvSource(
        "Test123456, true",
        "test123456, false",
        "123456, false",
        "Test-123456, true",
        "TEST12345, false"
    )
    fun testValidatePassword(password: String, expectedIsValid: Boolean) {
        val state = userDataValidator.validatePassword(password)

        assertThat(state.isValid).isEqualTo(expectedIsValid)
    }
}