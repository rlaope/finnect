package core

import java.net.Socket
import java.time.Instant
import java.util.*

class TcpSession(
    val socket: Socket
) {
    val sessionId: String = UUID.randomUUID().toString()
    val remoteIp: String = socket.inetAddress.hostAddress
    val connectedAt: Instant = Instant.now()
    var lastActiveAt: Instant = connectedAt

    fun updateLastActive() {
        lastActiveAt = Instant.now()
    }

    fun isAlive(): Boolean {
        return !socket.isClosed && socket.isConnected
    }

    fun close() {
        socket.close()
    }
}