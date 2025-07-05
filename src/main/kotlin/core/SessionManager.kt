package core

import java.util.concurrent.ConcurrentHashMap

object SessionManager {
    private val sessions = ConcurrentHashMap<String, TcpSession>()

    fun register(session: TcpSession) {
        sessions[session.sessionId] = session
        println("[SessionManager] 등록됨: ${session.sessionId}")
    }

    fun unregister(session: TcpSession) {
        sessions.remove(session.sessionId)
        println("[SessionManager] 해제됨: ${session.sessionId}")
    }

    fun getSession(id: String): TcpSession? = sessions[id]

    fun getAllSessions(): Collection<TcpSession> = sessions.values

    fun activeCount(): Int = sessions.size

    fun broadcast(message: String) {
        sessions.values.forEach {
            if (it.isAlive()) {
                try {
                    val output = it.socket.getOutputStream()
                    output.write(message.toByteArray())
                    output.flush()
                } catch (e: Exception) {
                    println("[SessionManager] 전송 실패: ${it.sessionId}")
                }
            }
        }
    }
}
