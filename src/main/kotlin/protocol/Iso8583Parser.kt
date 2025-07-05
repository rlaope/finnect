package protocol

object Iso8583Parser {
    fun parse(raw: String): Iso8583Message {
        println("Parse 되어가고 있습니다 ㅁㄴㅇㄹㅁㄴㅇㄹㅁㄴㅇㄹ")
        val mti = raw.substring(0, 4)
        val bitmap = raw.substring(4, 20)
        val fieldData = raw.substring(20)

        val activeFields = parseBitmap(bitmap)

        val fields = mutableMapOf<Int, String>()
        var cursor = 0

        for (field in activeFields) {
            val def = IsoFieldDictionary.getDefinition(field) ?: continue

            val value = when (def.type) {
                FieldType.FIXED -> {
                    val v = fieldData.substring(cursor, cursor + def.length)
                    cursor += def.length
                    v
                }

                FieldType.LLVAR -> {
                    val len = fieldData.substring(cursor, cursor + 2).toInt()
                    cursor += 2
                    val v = fieldData.substring(cursor, cursor + len)
                    cursor += len
                    v
                }

                FieldType.LLLVAR -> {
                    val len = fieldData.substring(cursor, cursor + 3).toInt()
                    cursor += 3
                    val v = fieldData.substring(cursor, cursor + len)
                    cursor += len
                    v
                }
            }

            fields[field] = value
        }

        return Iso8583Message(mti, fields)
    }

    private fun parseBitmap(bitmapHex: String): List<Int> {
        val binary = bitmapHex.chunked(2)
            .joinToString("") { it.toInt(16).toString(2).padStart(8, '0') }

        return binary.mapIndexedNotNull { index, bit ->
            if (bit == '1') index + 1 else null
        }
    }
}