package core

import java.io.InputStream

class MessageDecoder {

    fun readFrame(input: InputStream): String? {
        val lengthHeader = ByteArray(4)
        if (!input.readFully(lengthHeader)) return null

        val length = String(lengthHeader).toIntOrNull() ?: return null
        if (length <= 4) return null

        val bodyLength = length - 4
        val body = ByteArray(bodyLength)
        if (!input.readFully(body)) return null

        return String(body)
    }

    private fun InputStream.readFully(buffer: ByteArray): Boolean {
        var bytesRead = 0
        while (bytesRead < buffer.size) {
            val result = this.read(buffer, bytesRead, buffer.size - bytesRead)
            if (result == -1) return false
            bytesRead += result
        }
        return true
    }
}
