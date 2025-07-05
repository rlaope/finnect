import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DailyFileRotator(private val baseDir: String, private val prefix: String) {
    private var currentDate: LocalDate = LocalDate.now()
    private var writer: PrintWriter = createWriter(currentDate)

    private fun createWriter(date: LocalDate): PrintWriter {
        val dir = File(baseDir)
        if (!dir.exists()) dir.mkdirs()

        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val file = File(dir, "$prefix-${date.format(formatter)}.log")

        return PrintWriter(FileWriter(file, true), true)
    }

    @Synchronized
    fun writeLine(line: String) {
        val today = LocalDate.now()
        if (today != currentDate) {
            writer.close()
            writer = createWriter(today)
            currentDate = today
        }

        writer.println(line)
    }

    fun close() {
        writer.close()
    }
}