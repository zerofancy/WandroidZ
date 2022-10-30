package top.ntutn.wandroidz.model

@kotlinx.serialization.Serializable
data class DataWrapperModel<T>(val data: T, val errorCode: Int = 0, val errorMessage: String = "")
