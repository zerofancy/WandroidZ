package top.ntutn.wandroidz.util

import kotlinx.serialization.json.Json

object JsonUtil {
    val json = Json {
        ignoreUnknownKeys = true
    }
}