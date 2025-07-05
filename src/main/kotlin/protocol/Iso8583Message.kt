package protocol

data class Iso8583Message(
    val mti: String,
    val fields: Map<Int, String>
) {
    fun getField(n: Int): String? = fields[n]

    override fun toString(): String {
        return "MTI: $mti\n" + fields.entries.joinToString("\n") { "F${it.key}: ${it.value}" }
    }
}