package top.ntutn.wandroidz.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@kotlinx.serialization.Serializable
@Parcelize
data class BannerDataModel(val id: Int, val imagePath: String, val url: String): Parcelable
