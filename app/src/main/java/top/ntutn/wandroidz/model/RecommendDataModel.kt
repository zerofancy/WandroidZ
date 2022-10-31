package top.ntutn.wandroidz.model

import androidx.recyclerview.widget.DiffUtil
import kotlinx.serialization.Serializable

@Serializable
data class RecommendDataModel(
    val id: Long,
    val title: String,
    val link: String? = null
)
