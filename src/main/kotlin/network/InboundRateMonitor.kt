package network

import java.time.Instant
import java.util.concurrent.ConcurrentLinkedQueue

object InboundRateMonitor {
    private val timestamps = ConcurrentLinkedQueue<Long>()
    private const val WINDOW_SIZE_MS = 10_000  // 10초 윈도우

    fun record() {
        val now = Instant.now().toEpochMilli()
        timestamps.add(now)

        // 오래된 항목 제거
        while (timestamps.isNotEmpty() && now - timestamps.peek() > WINDOW_SIZE_MS) {
            timestamps.poll()
        }
    }

    fun currentTps(): Double {
        val now = Instant.now().toEpochMilli()
        val cutoff = now - 1_000  // 최근 1초간 요청
        val count = timestamps.count { it >= cutoff }
        return count.toDouble()
    }

    fun averageTps(): Double {
        val now = Instant.now().toEpochMilli()
        val cutoff = now - WINDOW_SIZE_MS
        val count = timestamps.count { it >= cutoff }
        return count / (WINDOW_SIZE_MS / 1000.0)
    }
}