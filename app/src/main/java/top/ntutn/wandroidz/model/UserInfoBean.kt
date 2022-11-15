package top.ntutn.wandroidz.model

@kotlinx.serialization.Serializable
data class UserInfoBean(val coinInfo: CoinInfoBean, val userInfo: UserBean)
