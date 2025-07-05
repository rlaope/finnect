package protocol

data class IsoFieldDefinition(
    val fieldNumber: Int,
    val type: FieldType,
    val length: Int // FIXED는 고정 길이, VAR은 최대 길이
)
