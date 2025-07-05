object ShutdownGuard {
    private val cleanupTasks = mutableListOf<() -> Unit>()

    fun registerCleanup(task: () -> Unit) {
        cleanupTasks.add(task)
    }

    fun init() {
        Runtime.getRuntime().addShutdownHook(Thread {
            println("[SHUTDOWN] 종료 감지됨, 정리 작업 시작")

            cleanupTasks.forEachIndexed { idx, task ->
                try {
                    task()
                    println("[SHUTDOWN] Task ${idx + 1} 완료")
                } catch (e: Exception) {
                    println("[SHUTDOWN] Task ${idx + 1} 실패: ${e.message}")
                }
            }

            println("[SHUTDOWN] 모든 종료 작업 완료")
        })
    }
}