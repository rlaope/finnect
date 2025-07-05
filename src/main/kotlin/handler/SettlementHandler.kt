package handler

import protocol.Iso8583Message

class SettlementHandler {
     /**
     * Settle the given message.
     * The message should start with "SETTLE" followed by a unique identifier.
     * Example: "SETTLE987654"
     *
     * @param msg The message to settle.
     * @return A confirmation string indicating the settlement status.
     */
    fun settle(msg: String): String {
        // TODO
        return "SETTLE_OK"
    }

    fun settleIso(iso: Iso8583Message): Iso8583Message {
        val trace = iso.getField(11) ?: "000000"
        return Iso8583Message(
            mti = "0410",
            fields = mapOf(
                11 to trace,
                39 to "00"
            )
        )
    }
}