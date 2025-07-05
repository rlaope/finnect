package mock

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random


object MockDataGenerator {

    fun generateAuthMessage(): String {
        val mti = "0200"
        val bitmap = "F238000000000000" // 필드 2, 3, 4, 7, 11, 41, 49 등 포함

        val cardNumber = randomNumeric(16)
        val processCode = "000000"
        val amount = randomNumeric(12)
        val transmissionDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmmss"))
        val traceNumber = randomNumeric(6)
        val terminalId = "TERM1234"
        val currency = "410" // KRW

        return buildString {
            append(mti)
            append(bitmap)
            append(fixed(cardNumber, 16))
            append(fixed(processCode, 6))
            append(fixed(amount, 12))
            append(fixed(transmissionDateTime, 10))
            append(fixed(traceNumber, 6))
            append(fixed(terminalId, 8))
            append(fixed(currency, 3))
        }
    }

    private fun randomNumeric(length: Int): String =
        (1..length).map { Random.nextInt(0, 10) }.joinToString("")

    private fun fixed(value: String, length: Int): String =
        value.padEnd(length, ' ')
}