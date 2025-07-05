package router

import handler.AuthHandler
import handler.SettlementHandler
import protocol.Iso8583Builder
import protocol.Iso8583Message
import protocol.Iso8583Parser

class MessageRouter(
    private val authHandler: AuthHandler = AuthHandler(),
    private val settlementHandler: SettlementHandler = SettlementHandler()
) {
    fun route(message: String): String {
        return when {
            message.startsWith("PING") -> {
                "PONG"
            }

            message.startsWith("AUTH") -> {
                val result = authHandler.authenticate(message)
                Iso8583Builder.build(
                    Iso8583Message("0210", mapOf(39 to "00"))
                )
            }

            message.startsWith("SETTLE") -> {
                val result = settlementHandler.settle(message)
                Iso8583Builder.build(
                    Iso8583Message("0410", mapOf(39 to "00"))
                )
            }

            message.matches(Regex("^\\d{4}[A-Fa-f0-9]{16}.*")) -> {
                val iso = Iso8583Parser.parse(message)
                println("[ISO] 파싱된 MTI: ${iso.mti}")
                println("[ISO] 필드: ${iso.fields}")

                val responseIso = when (iso.mti) {
                    "0200" -> authHandler.authenticateIso(iso)
                    "0400" -> settlementHandler.settleIso(iso)
                    else -> Iso8583Message("9999", mapOf(39 to "96")) // 처리 불가
                }
                Iso8583Builder.build(responseIso)
            }

            else -> {
                "UNKNOWN_COMMAND"
            }
        }
    }
}