package com.coldboar.coreguard

import org.junit.Assert.assertEquals
import org.junit.Test

class MemoryUsageCalculatorTest {
    @Test
    fun calculatesUsedMemoryInMegabytes() {
        val result = MemoryUsageCalculator.fromBytes(
            availableBytes = 256L * 1024L * 1024L,
            totalBytes = 1024L * 1024L * 1024L
        )
        assertEquals(768L, result.usedMb)
        assertEquals(1024L, result.totalMb)
    }

    @Test
    fun clampsInvalidMemoryValues() {
        val result = MemoryUsageCalculator.fromBytes(
            availableBytes = 2L * 1024L * 1024L,
            totalBytes = 1L * 1024L * 1024L
        )
        assertEquals(0L, result.usedMb)
        assertEquals(1L, result.totalMb)
    }
}
