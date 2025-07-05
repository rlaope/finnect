package app

import core.MessageDecoder
import core.SessionManager
import core.TcpSession
import router.MessageRouter
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class TcpServer(
    private val port: Int
) {

    fun start() {
        val serverSocket = ServerSocket(port)
        println("[Finncet] TCP Server started on port $port")

        while (true) {
            val client = serverSocket.accept()
            println("[Finncet] Client connected: ${client.inetAddress.hostAddress}")
            handleClient(client)
        }
    }

    private fun handleClient(socket: Socket) {
        thread {
            val session = TcpSession(socket)
            SessionManager.register(session)

            println("[SESSION] 연결됨: ${session.sessionId} from ${session.remoteIp}")

            try {
                socket.use {
                    val input = socket.getInputStream()
                    val output = socket.getOutputStream()
                    val decoder = MessageDecoder()
                    val router = MessageRouter()

                    val message = decoder.readFrame(input)
                    session.updateLastActive()

                    println("[RECV ${session.sessionId}] $message")
                    val response = message?.let { router.route(it) } ?: "INVALID"

                    output.write(response.toByteArray())
                    output.flush()
                }
            } finally {
                SessionManager.unregister(session)
                println("[SESSION] 종료됨: ${session.sessionId}")
            }
        }
    }
}