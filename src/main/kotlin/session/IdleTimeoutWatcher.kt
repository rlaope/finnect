package session

import core.TcpSession
import java.time.Duration
import java.time.Instant
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class IdleTimeoutWatcher(
    private val sessions: () -> Collection<TcpSession>,
    private val timeoutSeconds: Long = 60
) {
    private val executor = Executors.newSingleThreadScheduledExecutor()

    fun start() {
        executor.scheduleAtFixedRate({
            val now = Instant.now()
            sessions().forEach {
                if (Duration.between(it.lastActiveAt, now).seconds > timeoutSeconds) {
                    println("세션 ${it.sessionId} 시간 초과로 종료")
                    it.close()
                }
            }
        }, 10, 10, TimeUnit.SECONDS)
    }

    fun stop() {
        executor.shutdown()
    }
}