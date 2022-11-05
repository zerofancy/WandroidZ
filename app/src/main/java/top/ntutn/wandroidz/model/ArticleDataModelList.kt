package top.ntutn.wandroidz.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticleDataModelList(val list: List<ArticleDataModel>): Parcelable
