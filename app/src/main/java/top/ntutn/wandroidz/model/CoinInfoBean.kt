package top.ntutn.wandroidz.model

@kotlinx.serialization.Serializable
data class CoinInfoBean(
    val coinCount: Long = 0,
    val level: Int = 0,
    val rank: String = "",
    val userId: Long = 0
)