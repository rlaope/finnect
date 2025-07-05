package handler

import protocol.Iso8583Message

class AuthHandler {
    fun authenticate(msg: String): String {
        // TODO
        return "AUTH_OK"
    }

    fun authenticateIso(iso: Iso8583Message): Iso8583Message {
        val cardNo = iso.getField(2) ?: return iso.copy(mti = "0210", fields = mapOf(39 to "14"))
        val amount = iso.getField(4) ?: return iso.copy(mti = "0210", fields = mapOf(39 to "13"))

        return Iso8583Message(
            mti = "0210",
            fields = mapOf(
                2 to cardNo,
                4 to amount,
                11 to (iso.getField(11) ?: "000000"),
                39 to "00"
            )
        )
    }
}