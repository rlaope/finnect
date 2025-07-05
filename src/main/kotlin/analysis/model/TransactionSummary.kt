package analysis.model

data class TransactionSummary(
    val totalCount: Int,
    val averageAmount: Long,
    val byType: Map<TransactionType, Int>
)
