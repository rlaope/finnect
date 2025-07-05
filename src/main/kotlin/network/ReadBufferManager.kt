package network

class ReadBufferManager {
    private val buffer = StringBuilder()

    fun append(data: String) {
        buffer.append(data)
    }

    fun nextMessage(delimiter: String = "\n"): String? {
        val index = buffer.indexOf(delimiter)
        return if (index >= 0) {
            val message = buffer.substring(0, index)
            buffer.delete(0, index + delimiter.length)
            message
        } else {
            null
        }
    }
}