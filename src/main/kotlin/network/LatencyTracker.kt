package network

import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentLinkedQueue

object LatencyTracker {
    private val latencies = ConcurrentLinkedQueue<Long>()
    private const val MAX_HISTORY = 5000

    fun record(start: Instant, end: Instant) {
        val duration = Duration.between(start, end).toMillis()
        latencies.add(duration)

        if (latencies.size > MAX_HISTORY) {
            repeat(latencies.size - MAX_HISTORY) { latencies.poll() }
        }
    }

    fun snapshot(): LatencySnapshot {
        val data = latencies.toList()
        if (data.isEmpty()) return LatencySnapshot(0, 0, 0)

        val avg = data.sum() / data.size
        val max = data.maxOrNull() ?: 0
        val mode = data.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key ?: 0

        return LatencySnapshot(avg, max, mode)
    }
}

data class LatencySnapshot(
    val avgMs: Long,
    val maxMs: Long,
    val mostFrequentMs: Long
)