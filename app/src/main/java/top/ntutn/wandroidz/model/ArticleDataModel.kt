package top.ntutn.wandroidz.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class ArticleDataModel(
    val id: Long,
    val title: String,
    val link: String,
    val author: String? = null,
    val shareUser: String? = null,
    val publishTime: Long? = null,
    val shareDate: Long? = null,
): Parcelable
