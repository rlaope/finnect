package network

import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.net.Socket

object ResponseWriter {
    fun write(socket: Socket, response: String) {
        if (!socket.isClosed) {
            try {
                val writer = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
                writer.write(response)
                writer.newLine()
                writer.flush()
            } catch (e: Exception) {
                println("[ERROR] 응답 전송 실패: ${e.message}")
            }
        }
    }
}