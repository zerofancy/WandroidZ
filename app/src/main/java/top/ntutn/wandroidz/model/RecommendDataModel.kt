package top.ntutn.wandroidz.model

import kotlinx.serialization.Serializable

@Serializable
data class RecommendDataModel(
    val id: Long,
    val title: String,
    val link: String,
    val shareUser: String? = null
)
