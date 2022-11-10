package top.ntutn.wandroidz.mainpage

import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import top.ntutn.wandroidz.api.HomePageApi
import top.ntutn.wandroidz.model.ArticleDataModel
import top.ntutn.wandroidz.model.ArticleDataModelList
import top.ntutn.wandroidz.util.wanAndroidApi

/**
 * 置顶文章
 * WanAndroid的置顶文章不会很快更新，因而加入cache优化体验
 */
class TopArticleDataSource {
    companion object {
        private const val KEY_CACHE_LIFE = "cache_life"
        private const val KEY_CACHED_ARTICLES = "cached_articles"
        private const val CACHE_LIFE = 24 * 60 * 60 * 1000 // 1天后过期
    }

    private val kv by lazy {
        MMKV.mmkvWithID("top_article_cache")
    }

    private val api by lazy(LazyThreadSafetyMode.NONE) {
        wanAndroidApi<HomePageApi>()
    }

    suspend fun load(): List<ArticleDataModel> = withContext(Dispatchers.IO) {
        val cacheLife = kv.decodeLong(KEY_CACHE_LIFE, 0)
        val cachedData = kv.decodeParcelable(KEY_CACHED_ARTICLES, ArticleDataModelList::class.java)
            ?.takeIf { cacheLife > System.currentTimeMillis() }
        if (cachedData?.list?.isNotEmpty() == true) {
            Timber.i("found cached article list")
            return@withContext cachedData.list
        }

        return@withContext kotlin.runCatching {
            api.getTopArticleList()
        }.onSuccess {
            kv.encode(KEY_CACHED_ARTICLES, ArticleDataModelList(it.data))
            kv.encode(KEY_CACHE_LIFE, System.currentTimeMillis() + CACHE_LIFE)
        }.onFailure {
            Timber.e(it, "fetch top data failed")
        }.getOrNull()?.data?: emptyList()
    }
}