package network

import java.time.Duration
import java.time.Instant
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

object SessionMetrics {
    private val totalConnections = AtomicInteger(0)
    private val totalRequests = AtomicLong(0)
    private val totalResponseTimeMillis = AtomicLong(0)
    private val maxResponseTimeMillis = AtomicLong(0)

    fun onConnect() {
        totalConnections.incrementAndGet()
    }

    fun onDisconnect() {
        totalConnections.decrementAndGet()
    }

    fun onRequest(start: Instant, end: Instant) {
        val duration = Duration.between(start, end).toMillis()
        totalRequests.incrementAndGet()
        totalResponseTimeMillis.addAndGet(duration)
        maxResponseTimeMillis.getAndUpdate { old -> maxOf(old, duration) }
    }

    fun snapshot(): MetricsSnapshot {
        val req = totalRequests.get()
        val avg = if (req == 0L) 0L else totalResponseTimeMillis.get() / req
        return MetricsSnapshot(
            activeConnections = totalConnections.get(),
            totalRequests = req,
            avgResponseTimeMillis = avg,
            maxResponseTimeMillis = maxResponseTimeMillis.get()
        )
    }
}

data class MetricsSnapshot(
    val activeConnections: Int,
    val totalRequests: Long,
    val avgResponseTimeMillis: Long,
    val maxResponseTimeMillis: Long
)