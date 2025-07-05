package protocol

object IsoFieldDictionary {
    private val fields = listOf(
        IsoFieldDefinition(2, FieldType.LLVAR, 19),     // card no
        IsoFieldDefinition(3, FieldType.FIXED, 6),       // status code
        IsoFieldDefinition(4, FieldType.FIXED, 12),      // transaction amount
        IsoFieldDefinition(11, FieldType.FIXED, 6),      // Trace No
        IsoFieldDefinition(48, FieldType.LLLVAR, 999)    // additional
    ).associateBy { it.fieldNumber }

    fun getDefinition(fieldNumber: Int): IsoFieldDefinition? = fields[fieldNumber]
}