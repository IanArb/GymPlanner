package com.ianarbuckle.gymplanner.web.ui.login

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FieldValidatorTest {

    private val validator = FieldValidator()

    @Test
    fun `valid username passes validation`() {
        assertTrue(validator.validateUsername("john_doe"))
    }

    @Test
    fun `valid username with dots passes validation`() {
        assertTrue(validator.validateUsername("john.doe"))
    }

    @Test
    fun `valid username with hyphens passes validation`() {
        assertTrue(validator.validateUsername("john-doe"))
    }

    @Test
    fun `valid username with numbers passes validation`() {
        assertTrue(validator.validateUsername("john123"))
    }

    @Test
    fun `valid username with mixed characters passes validation`() {
        assertTrue(validator.validateUsername("John_Doe-99"))
    }

    @Test
    fun `valid username with exactly 3 characters passes validation`() {
        assertTrue(validator.validateUsername("abc"))
    }

    @Test
    fun `valid username with 30 characters passes validation`() {
        assertTrue(validator.validateUsername("a".repeat(30)))
    }

    @Test
    fun `blank username fails validation`() {
        assertFalse(validator.validateUsername(""))
    }

    @Test
    fun `whitespace only username fails validation`() {
        assertFalse(validator.validateUsername("   "))
    }

    @Test
    fun `username with spaces fails validation`() {
        assertFalse(validator.validateUsername("john doe"))
    }

    @Test
    fun `username with 2 characters fails validation`() {
        assertFalse(validator.validateUsername("ab"))
    }

    @Test
    fun `username with 31 characters fails validation`() {
        assertFalse(validator.validateUsername("a".repeat(31)))
    }

    @Test
    fun `username starting with a dot fails validation`() {
        assertFalse(validator.validateUsername(".username"))
    }

    @Test
    fun `username starting with a hyphen fails validation`() {
        assertFalse(validator.validateUsername("-username"))
    }

    @Test
    fun `username with special characters fails validation`() {
        assertFalse(validator.validateUsername("user@name"))
    }

    @Test
    fun `password with exactly 6 characters passes validation`() {
        assertTrue(validator.validatePassword("abc123"))
    }

    @Test
    fun `password longer than 6 characters passes validation`() {
        assertTrue(validator.validatePassword("securepassword"))
    }

    @Test
    fun `password with 5 characters fails validation`() {
        assertFalse(validator.validatePassword("abc12"))
    }

    @Test
    fun `empty password fails validation`() {
        assertFalse(validator.validatePassword(""))
    }

    @Test
    fun `password with spaces passes validation when length is sufficient`() {
        assertTrue(validator.validatePassword("abc 12"))
    }
}
