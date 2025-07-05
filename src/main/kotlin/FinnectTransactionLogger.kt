import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

object FinnectTransactionLogger {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
    private val logDir = File("logs")
    private val lock = ReentrantLock()

    init {
        if (!logDir.exists()) {
            logDir.mkdirs()
        }
    }

    fun log(sessionId: String, request: String, response: String) {
        val now = LocalDateTime.now()
        val logFile = File(logDir, "transactions-${now.format(DateTimeFormatter.ofPattern("yyyyMMdd"))}.log")

        val logLine = buildString {
            append("[${now.format(formatter)}] ")
            append("Session=$sessionId ")
            append("REQ=$request ")
            append("RESP=$response")
        }

        lock.withLock {
            PrintWriter(FileWriter(logFile, true)).use {
                it.println(logLine)
            }
        }
    }
}