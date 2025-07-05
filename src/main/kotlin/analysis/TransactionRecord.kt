package analysis

import analysis.model.TransactionType
import java.time.LocalDateTime

data class TransactionRecord(
    val userId: String,
    val type: TransactionType,
    val amount: Long,
    val timestamp: LocalDateTime
)

interface TransactionRepository {
    fun findByPeriod(start: LocalDateTime, end: LocalDateTime): List<TransactionRecord>
}