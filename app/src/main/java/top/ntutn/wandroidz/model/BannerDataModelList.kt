package top.ntutn.wandroidz.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * for banner cache
 */
@Parcelize
data class BannerDataModelList(val list: List<BannerDataModel>): Parcelable
