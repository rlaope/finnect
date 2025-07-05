package network

import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class HeartbeatBroadcaster(
    private val targetUrl: String,
    private val intervalSeconds: Long = 30
) {
    private val scheduler = Executors.newSingleThreadScheduledExecutor()

    fun start() {
        scheduler.scheduleAtFixedRate({
            try {
                val snapshot = SessionMetrics.snapshot()

                val payload = """
                    {
                      "timestamp": "${Date()}",
                      "activeConnections": ${snapshot.activeConnections},
                      "totalRequests": ${snapshot.totalRequests},
                      "avgResponseTimeMs": ${snapshot.avgResponseTimeMillis},
                      "maxResponseTimeMs": ${snapshot.maxResponseTimeMillis}
                    }
                """.trimIndent()

                val conn = URL(targetUrl).openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true

                conn.outputStream.use { os ->
                    os.write(payload.toByteArray(Charsets.UTF_8))
                }

                val responseCode = conn.responseCode
                if (responseCode != 200) {
                    println("[HEARTBEAT] 전송 실패: $responseCode")
                } else {
                    println("[HEARTBEAT] 전송 완료")
                }

            } catch (e: Exception) {
                println("[HEARTBEAT] 오류 발생: ${e.message}")
            }
        }, 5, intervalSeconds, TimeUnit.SECONDS)
    }

    fun stop() {
        scheduler.shutdown()
    }
}