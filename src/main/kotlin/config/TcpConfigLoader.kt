package config

import java.io.FileInputStream
import java.util.*

object TcpConfigLoader {
    private val props = Properties()

    init {
        val configFile = System.getenv("TCP_CONFIG_PATH") ?: "config/application.properties"
        FileInputStream(configFile).use {
            props.load(it)
        }
    }

    fun getInt(key: String, default: Int): Int {
        return props.getProperty(key)?.toIntOrNull() ?: default
    }

    fun getString(key: String, default: String): String {
        return props.getProperty(key) ?: default
    }

    fun getBoolean(key: String, default: Boolean): Boolean {
        return props.getProperty(key)?.toBooleanStrictOrNull() ?: default
    }
}