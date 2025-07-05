package network

import core.TcpSession
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

class BackPressureControlManager (
    private val limitPerMinute: Int = 100
) {
    private val messageCounters = ConcurrentHashMap<String, MutableList<Instant>>()

    fun registerMessage(session: TcpSession): Boolean {
        val now = Instant.now()
        val history = messageCounters.computeIfAbsent(session.sessionId) { mutableListOf() }

        history.removeIf { Duration.between(it, now).seconds > 60 }

        history.add(now)

        if (history.size > limitPerMinute) {
            println("[PRESSURE] 세션 ${session.sessionId} 과다요청 (${history.size} msg/min)")
            return false
        }

        return true
    }

    fun reset(sessionId: String) {
        messageCounters.remove(sessionId)
    }
}