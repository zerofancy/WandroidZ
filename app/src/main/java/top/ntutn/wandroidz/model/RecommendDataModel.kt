package top.ntutn.wandroidz.model

import androidx.recyclerview.widget.DiffUtil
import kotlinx.serialization.Serializable

@Serializable
data class RecommendDataModel(
    val id: Long,
    val title: String,
    val link: String? = null
) {
    class DiffCallback: DiffUtil.ItemCallback<RecommendDataModel>() {
        override fun areItemsTheSame(
            oldItem: RecommendDataModel,
            newItem: RecommendDataModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RecommendDataModel,
            newItem: RecommendDataModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}
