package network

import core.TcpSession
import java.time.Duration
import java.time.Instant
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class ConnectionHealthChecker(
    private val sessions: () -> Collection<TcpSession>,
    private val maxIdleSeconds: Long = 120
) {
    private val executor = Executors.newSingleThreadScheduledExecutor()

    fun start() {
        executor.scheduleAtFixedRate({
            val now = Instant.now()
            sessions().forEach { session ->
                val idle = Duration.between(session.lastActiveAt, now).seconds
                if (idle > maxIdleSeconds) {
                    println("[HEALTH] 세션 ${session.sessionId} 비정상 종료 (idle $idle s)")
                    session.close()
                }
            }
        }, 30, 30, TimeUnit.SECONDS)
    }

    fun stop() {
        executor.shutdown()
    }
}
