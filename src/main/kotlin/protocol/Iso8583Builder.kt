package protocol

object Iso8583Builder {

    fun build(iso: Iso8583Message): String {
        val mti = iso.mti
        val fields = iso.fields.toSortedMap()

        val bitmapBinary = CharArray(64) { '0' }
        val body = StringBuilder()

        for ((fieldNum, value) in fields) {
            bitmapBinary[fieldNum - 1] = '1'

            val def = IsoFieldDictionary.getDefinition(fieldNum)
                ?: error("Field $fieldNum is undefined")

            val encoded = when (def.type) {
                FieldType.FIXED -> value.padEnd(def.length, ' ')
                FieldType.LLVAR -> value.length.toString().padStart(2, '0') + value
                FieldType.LLLVAR -> value.length.toString().padStart(3, '0') + value
            }

            body.append(encoded)
        }

        val bitmapHex = bitmapBinary.joinToString("")
            .chunked(4)
            .map { it.toInt(2).toString(16).padStart(1, '0') }
            .joinToString("")
            .uppercase()

        return mti + bitmapHex + body.toString()
    }
}