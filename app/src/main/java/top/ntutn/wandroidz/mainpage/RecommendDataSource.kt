package top.ntutn.wandroidz.mainpage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import top.ntutn.wandroidz.api.HomePageApi
import top.ntutn.wandroidz.model.RecommendDataModel

class RecommendDataSource {
    suspend fun load(currentPage: Int): List<RecommendDataModel> = withContext(Dispatchers.IO) {
        val result = kotlin.runCatching {
            HomePageApi.get().getRecommendList(currentPage)
        }.onFailure {
            Timber.e(it, "fetch recommend data failed, page=%d", currentPage)
        }.getOrNull()
        if (result?.errorCode == 0) {
            return@withContext result.data.datas
        }
        return@withContext emptyList()
    }
}