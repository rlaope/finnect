package analysis

import analysis.model.TransactionSummary
import java.time.LocalDateTime

class TransactionAnalyticsService(
    private val repository: TransactionRepository
) {
    fun summarize(start: LocalDateTime, end: LocalDateTime): TransactionSummary {
        val data = repository.findByPeriod(start, end)

        val totalCount = data.size
        val totalAmount = data.sumOf { it.amount }
        val avgAmount = if (totalCount > 0) totalAmount / totalCount else 0L

        val byType = data.groupingBy { it.type }
            .eachCount()

        return TransactionSummary(
            totalCount = totalCount,
            averageAmount = avgAmount,
            byType = byType
        )
    }
}