package top.ntutn.wandroidz.mainpage

import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import top.ntutn.wandroidz.api.HomePageApi
import top.ntutn.wandroidz.model.BannerDataModel
import top.ntutn.wandroidz.model.BannerDataModelList

/**
 * Banner
 * Banner不会很快更新，所以加入cache优化体验
 */
class BannerDataSource {
    companion object {
        private const val KEY_CACHE_LIFE = "cache_life"
        private const val KEY_CACHED_BANNERS = "cached_banners"
        private const val CACHE_LIFE = 24 * 60 * 60 * 1000 // 1天后过期
    }

    private val kv by lazy {
        MMKV.mmkvWithID("banner_cache")
    }

    private val api by lazy(LazyThreadSafetyMode.NONE) {
        HomePageApi.get()
    }

    suspend fun getBannerData(): List<BannerDataModel> = withContext(Dispatchers.IO) {
        val cacheLife = kv.decodeLong(KEY_CACHE_LIFE, 0)
        val cachedData = kv.decodeParcelable(KEY_CACHED_BANNERS, BannerDataModelList::class.java)
            ?.takeIf { cacheLife > System.currentTimeMillis() }
        if (cachedData?.list?.isNotEmpty() == true) {
            Timber.i("found cached banner data")
            return@withContext cachedData.list
        }

        val data = kotlin.runCatching {
            api.getBanner().data
        }.onFailure {
            Timber.e(it, "fetch banner failed")
        }.getOrNull()?.takeIf { it.isNotEmpty() }

        data?.let {
            kv.encode(KEY_CACHED_BANNERS, BannerDataModelList(it))
            kv.encode(KEY_CACHE_LIFE, System.currentTimeMillis() + CACHE_LIFE)
        }
        data ?: emptyList()
    }
}