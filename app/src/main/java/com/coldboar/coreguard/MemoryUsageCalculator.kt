package com.coldboar.coreguard

data class MemoryUsage(val usedMb: Long, val totalMb: Long)

object MemoryUsageCalculator {
    fun fromBytes(availableBytes: Long, totalBytes: Long): MemoryUsage {
        val safeTotal = (totalBytes / (1024L * 1024L)).coerceAtLeast(0L)
        val safeAvailable = (availableBytes / (1024L * 1024L)).coerceIn(0L, safeTotal)
        return MemoryUsage(
            usedMb = (safeTotal - safeAvailable).coerceIn(0L, safeTotal),
            totalMb = safeTotal
        )
    }
}
