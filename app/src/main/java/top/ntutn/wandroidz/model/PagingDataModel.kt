package top.ntutn.wandroidz.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class PagingDataModel<T>(
    @SerialName("curPage")
    val currentPage: Int = 1,
    val datas: List<T>,
    val offset: Int = 0,
    val over: Boolean?,
    val pageCount: Int?,
    val size: Int?,
    val total: Int?
)
