package com.ianarbuckle.gymplanner.common

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ApiResultTest {
    private val error = IllegalStateException("boom")
    private val otherError = IllegalArgumentException("bad input")

    // ========== Construction ==========

    @Test
    fun `success holds the value it was built with`() {
        val result = ApiResult.Success("value")

        assertEquals("value", result.value)
    }

    @Test
    fun `failure holds the error it was built with`() {
        val result = ApiResult.Failure(error)

        assertSame(error, result.error)
    }

    @Test
    fun `success can hold a null value`() {
        val result = ApiResult.Success<String?>(null)

        assertNull(result.value)
    }

    @Test
    fun `failure is covariant so it stands in for any success type`() {
        val stringResult: ApiResult<String> = ApiResult.Failure(error)
        val intResult: ApiResult<Int> = ApiResult.Failure(error)

        assertTrue(stringResult.isFailure)
        assertTrue(intResult.isFailure)
    }

    // ========== isSuccess / isFailure ==========

    @Test
    fun `isSuccess is true and isFailure is false for a success`() {
        val result: ApiResult<String> = ApiResult.Success("value")

        assertTrue(result.isSuccess)
        assertFalse(result.isFailure)
    }

    @Test
    fun `isSuccess is false and isFailure is true for a failure`() {
        val result: ApiResult<String> = ApiResult.Failure(error)

        assertFalse(result.isSuccess)
        assertTrue(result.isFailure)
    }

    @Test
    fun `isSuccess is true for a success wrapping null`() {
        val result: ApiResult<String?> = ApiResult.Success(null)

        assertTrue(result.isSuccess)
        assertFalse(result.isFailure)
    }

    // ========== getOrNull ==========

    @Test
    fun `getOrNull returns the value for a success`() {
        val result: ApiResult<String> = ApiResult.Success("value")

        assertEquals("value", result.getOrNull())
    }

    @Test
    fun `getOrNull returns null for a failure`() {
        val result: ApiResult<String> = ApiResult.Failure(error)

        assertNull(result.getOrNull())
    }

    @Test
    fun `getOrNull cannot distinguish a null value from a failure`() {
        val nullSuccess: ApiResult<String?> = ApiResult.Success(null)
        val failure: ApiResult<String?> = ApiResult.Failure(error)

        // Both return null - use isSuccess or fold when the value type is nullable.
        assertNull(nullSuccess.getOrNull())
        assertNull(failure.getOrNull())
        assertTrue(nullSuccess.isSuccess)
        assertTrue(failure.isFailure)
    }

    // ========== exceptionOrNull ==========

    @Test
    fun `exceptionOrNull returns the error for a failure`() {
        val result: ApiResult<String> = ApiResult.Failure(error)

        assertSame(error, result.exceptionOrNull())
    }

    @Test
    fun `exceptionOrNull returns null for a success`() {
        val result: ApiResult<String> = ApiResult.Success("value")

        assertNull(result.exceptionOrNull())
    }

    @Test
    fun `exceptionOrNull preserves the concrete exception type`() {
        val result: ApiResult<String> = ApiResult.Failure(otherError)

        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    // ========== getOrThrow ==========

    @Test
    fun `getOrThrow returns the value for a success`() {
        val result: ApiResult<String> = ApiResult.Success("value")

        assertEquals("value", result.getOrThrow())
    }

    @Test
    fun `getOrThrow returns null for a success wrapping null`() {
        val result: ApiResult<String?> = ApiResult.Success(null)

        assertNull(result.getOrThrow())
    }

    @Test
    fun `getOrThrow throws the original error for a failure`() {
        val result: ApiResult<String> = ApiResult.Failure(error)

        val thrown = assertFailsWith<IllegalStateException> { result.getOrThrow() }

        assertSame(error, thrown)
        assertEquals("boom", thrown.message)
    }

    // ========== map ==========

    @Test
    fun `map transforms the value of a success`() {
        val result: ApiResult<String> = ApiResult.Success("value")

        val mapped = result.map { it.length }

        assertEquals(5, mapped.getOrNull())
        assertTrue(mapped.isSuccess)
    }

    @Test
    fun `map can change the value type`() {
        val result: ApiResult<Int> = ApiResult.Success(42)

        val mapped: ApiResult<String> = result.map { "n=$it" }

        assertEquals("n=42", mapped.getOrNull())
    }

    @Test
    fun `map leaves a failure untouched and keeps the same error`() {
        val result: ApiResult<String> = ApiResult.Failure(error)

        val mapped = result.map { it.length }

        assertTrue(mapped.isFailure)
        assertSame(error, mapped.exceptionOrNull())
    }

    @Test
    fun `map does not invoke the transform for a failure`() {
        val result: ApiResult<String> = ApiResult.Failure(error)
        var invocations = 0

        result.map { invocations++ }

        assertEquals(0, invocations)
    }

    @Test
    fun `map invokes the transform exactly once for a success`() {
        val result: ApiResult<String> = ApiResult.Success("value")
        var invocations = 0

        result.map { invocations++ }

        assertEquals(1, invocations)
    }

    @Test
    fun `map returns the same failure instance rather than a copy`() {
        val result: ApiResult<String> = ApiResult.Failure(error)

        val mapped: ApiResult<Any> = result.map { it.length }

        assertSame<ApiResult<Any>>(result, mapped)
    }

    @Test
    fun `map propagates an exception thrown by the transform`() {
        val result: ApiResult<String> = ApiResult.Success("value")

        val thrown = assertFailsWith<IllegalStateException> {
            result.map<String, Int> { throw error }
        }

        assertSame(error, thrown)
    }

    @Test
    fun `map can be chained`() {
        val result: ApiResult<String> = ApiResult.Success("value")

        val mapped = result.map { it.length }.map { it * 2 }

        assertEquals(10, mapped.getOrNull())
    }

    @Test
    fun `map can produce a null value`() {
        val result: ApiResult<String> = ApiResult.Success("value")

        val mapped: ApiResult<String?> = result.map { null }

        assertTrue(mapped.isSuccess)
        assertNull(mapped.getOrNull())
    }

    // ========== fold ==========

    @Test
    fun `fold takes the success branch for a success`() {
        val result: ApiResult<String> = ApiResult.Success("value")

        val folded = result.fold(onSuccess = { "ok:$it" }, onFailure = { "err:${it.message}" })

        assertEquals("ok:value", folded)
    }

    @Test
    fun `fold takes the failure branch for a failure`() {
        val result: ApiResult<String> = ApiResult.Failure(error)

        val folded = result.fold(onSuccess = { "ok:$it" }, onFailure = { "err:${it.message}" })

        assertEquals("err:boom", folded)
    }

    @Test
    fun `fold does not invoke the failure branch for a success`() {
        val result: ApiResult<String> = ApiResult.Success("value")
        var failureInvocations = 0

        result.fold(onSuccess = { }, onFailure = { failureInvocations++ })

        assertEquals(0, failureInvocations)
    }

    @Test
    fun `fold does not invoke the success branch for a failure`() {
        val result: ApiResult<String> = ApiResult.Failure(error)
        var successInvocations = 0

        result.fold(onSuccess = { successInvocations++ }, onFailure = { })

        assertEquals(0, successInvocations)
    }

    @Test
    fun `fold passes the original error to the failure branch`() {
        val result: ApiResult<String> = ApiResult.Failure(error)

        val folded = result.fold(onSuccess = { null }, onFailure = { it })

        assertSame(error, folded)
    }

    @Test
    fun `fold can collapse both branches to a common type`() {
        val success: ApiResult<String> = ApiResult.Success("value")
        val failure: ApiResult<String> = ApiResult.Failure(error)

        val fromSuccess = success.fold(onSuccess = { it.length }, onFailure = { -1 })
        val fromFailure = failure.fold(onSuccess = { it.length }, onFailure = { -1 })

        assertEquals(5, fromSuccess)
        assertEquals(-1, fromFailure)
    }

    // ========== onSuccess ==========

    @Test
    fun `onSuccess invokes the action with the value for a success`() {
        var captured: String? = null

        ApiResult.Success("value").onSuccess { captured = it }

        assertEquals("value", captured)
    }

    @Test
    fun `onSuccess does not invoke the action for a failure`() {
        val result: ApiResult<String> = ApiResult.Failure(error)
        var invocations = 0

        result.onSuccess { invocations++ }

        assertEquals(0, invocations)
    }

    @Test
    fun `onSuccess invokes the action exactly once`() {
        val result: ApiResult<String> = ApiResult.Success("value")
        var invocations = 0

        result.onSuccess { invocations++ }

        assertEquals(1, invocations)
    }

    @Test
    fun `onSuccess returns the original instance so calls can be chained`() {
        val result: ApiResult<String> = ApiResult.Success("value")

        val returned = result.onSuccess { }

        assertSame(result, returned)
    }

    @Test
    fun `onSuccess returns the original instance for a failure too`() {
        val result: ApiResult<String> = ApiResult.Failure(error)

        val returned = result.onSuccess { }

        assertSame(result, returned)
    }

    @Test
    fun `onSuccess runs even when the value is null`() {
        var invocations = 0
        var captured: String? = "not null"

        ApiResult.Success<String?>(null).onSuccess {
            invocations++
            captured = it
        }

        assertEquals(1, invocations)
        assertNull(captured)
    }

    @Test
    fun `onSuccess propagates an exception thrown by the action`() {
        val result: ApiResult<String> = ApiResult.Success("value")

        val thrown = assertFailsWith<IllegalStateException> { result.onSuccess { throw error } }

        assertSame(error, thrown)
    }

    // ========== onFailure ==========

    @Test
    fun `onFailure invokes the action with the error for a failure`() {
        var captured: Throwable? = null

        ApiResult.Failure(error).onFailure { captured = it }

        assertSame(error, captured)
    }

    @Test
    fun `onFailure does not invoke the action for a success`() {
        val result: ApiResult<String> = ApiResult.Success("value")
        var invocations = 0

        result.onFailure { invocations++ }

        assertEquals(0, invocations)
    }

    @Test
    fun `onFailure invokes the action exactly once`() {
        val result: ApiResult<String> = ApiResult.Failure(error)
        var invocations = 0

        result.onFailure { invocations++ }

        assertEquals(1, invocations)
    }

    @Test
    fun `onFailure returns the original instance so calls can be chained`() {
        val result: ApiResult<String> = ApiResult.Failure(error)

        val returned = result.onFailure { }

        assertSame(result, returned)
    }

    @Test
    fun `onFailure returns the original instance for a success too`() {
        val result: ApiResult<String> = ApiResult.Success("value")

        val returned = result.onFailure { }

        assertSame(result, returned)
    }

    @Test
    fun `onFailure propagates an exception thrown by the action`() {
        val result: ApiResult<String> = ApiResult.Failure(error)

        val thrown = assertFailsWith<IllegalArgumentException> { result.onFailure { throw otherError } }

        assertSame(otherError, thrown)
    }

    // ========== Chaining ==========

    @Test
    fun `onSuccess and onFailure chain together on a success`() {
        var successValue: String? = null
        var failureError: Throwable? = null

        ApiResult.Success("value")
            .onSuccess { successValue = it }
            .onFailure { failureError = it }

        assertEquals("value", successValue)
        assertNull(failureError)
    }

    @Test
    fun `onSuccess and onFailure chain together on a failure`() {
        var successValue: String? = null
        var failureError: Throwable? = null

        val result: ApiResult<String> = ApiResult.Failure(error)
        result
            .onSuccess { successValue = it }
            .onFailure { failureError = it }

        assertNull(successValue)
        assertSame(error, failureError)
    }

    @Test
    fun `map and side effects compose in a single chain`() {
        val observed = mutableListOf<String>()

        val folded = ApiResult.Success("value")
            .onSuccess { observed += "before:$it" }
            .map { it.length }
            .onSuccess { observed += "after:$it" }
            .fold(onSuccess = { "ok:$it" }, onFailure = { "err" })

        assertEquals(listOf("before:value", "after:5"), observed)
        assertEquals("ok:5", folded)
    }

    @Test
    fun `a failure short-circuits every stage of a chain`() {
        val observed = mutableListOf<String>()
        val result: ApiResult<String> = ApiResult.Failure(error)

        val folded = result
            .onSuccess { observed += "before" }
            .map {
                observed += "map"
                it.length
            }
            .onSuccess { observed += "after" }
            .fold(onSuccess = { "ok" }, onFailure = { "err:${it.message}" })

        assertTrue(observed.isEmpty(), "no success-path work should run, but observed $observed")
        assertEquals("err:boom", folded)
    }

    // ========== Equality ==========

    @Test
    fun `successes with equal values are equal`() {
        assertEquals(ApiResult.Success("value"), ApiResult.Success("value"))
        assertEquals(ApiResult.Success("value").hashCode(), ApiResult.Success("value").hashCode())
    }

    @Test
    fun `successes with different values are not equal`() {
        assertNotEquals(ApiResult.Success("value"), ApiResult.Success("other"))
    }

    @Test
    fun `failures with the same error are equal`() {
        assertEquals(ApiResult.Failure(error), ApiResult.Failure(error))
    }

    @Test
    fun `failures with different errors are not equal`() {
        assertNotEquals(ApiResult.Failure(error), ApiResult.Failure(otherError))
    }

    @Test
    fun `a success is never equal to a failure`() {
        val success: ApiResult<String> = ApiResult.Success("value")
        val failure: ApiResult<String> = ApiResult.Failure(error)

        assertNotEquals<ApiResult<String>>(success, failure)
    }
}
