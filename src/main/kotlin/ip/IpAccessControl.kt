package ip

import java.net.InetAddress
import java.util.concurrent.CopyOnWriteArraySet

object IpAccessControl {
    private val whitelist = CopyOnWriteArraySet<String>()
    private val blacklist = CopyOnWriteArraySet<String>()

    fun allow(ip: String) {
        whitelist.add(ip)
        blacklist.remove(ip)
    }

    fun deny(ip: String) {
        blacklist.add(ip)
        whitelist.remove(ip)
    }

    fun isAllowed(remoteAddr: InetAddress): Boolean {
        val ip = remoteAddr.hostAddress
        return when {
            blacklist.contains(ip) -> false
            whitelist.isEmpty() -> true // 기본 허용
            whitelist.contains(ip) -> true
            else -> false
        }
    }

    fun loadFromFile(path: String) {
        val file = java.io.File(path)
        if (!file.exists()) return

        file.readLines()
            .map { it.trim() }
            .filter { it.isNotEmpty() && !it.startsWith("#") }
            .forEach { line ->
                if (line.startsWith("allow:")) {
                    allow(line.removePrefix("allow:").trim())
                } else if (line.startsWith("deny:")) {
                    deny(line.removePrefix("deny:").trim())
                }
            }
    }
}
