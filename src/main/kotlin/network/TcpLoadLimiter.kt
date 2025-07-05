package network

import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit

object TcpLoadLimiter {
    private const val MAX_CONCURRENT = 200  // 동시에 처리할 최대 요청 수
    private val semaphore = Semaphore(MAX_CONCURRENT)

    fun tryAcquire(timeoutMs: Long = 100): Boolean {
        return semaphore.tryAcquire(timeoutMs, TimeUnit.MILLISECONDS)
    }

    fun release() {
        semaphore.release()
    }
}