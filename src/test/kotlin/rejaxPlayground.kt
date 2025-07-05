
fun main() {
    val pattern = Regex("^\\d{4}[A-Fa-f0-9]{16}.*")

    val testMessages = listOf(
        "0200A000000000000000061234560000000000000005000000001", // ✅ 정상
        " 0200A000000000000000061234560000000000000005000000001", // ❌ 공백
        "0200A00000000000000", // ❌ bitmap 16자리 미만
        "0200A000000000000000", // ✅ bitmap만 있고 필드 없음
        "0200G000000000000000", // ❌ G는 16진수 아님
        "abcdA000000000000000"  // ❌ MTI가 숫자 아님
    )

    for (msg in testMessages) {
        val result = pattern.matches(msg)
        println("[$msg] → matches? $result")
    }
}
