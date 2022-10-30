package top.ntutn.wandroidz.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import top.ntutn.wandroidz.model.DataWrapperModel
import top.ntutn.wandroidz.model.PagingDataModel
import top.ntutn.wandroidz.model.RecommendDataModel
import top.ntutn.wandroidz.util.JsonUtil

interface HomePageApi {
    companion object {
        @OptIn(ExperimentalSerializationApi::class)
        fun get(): HomePageApi {
            val contentType = MediaType.get("application/json")
            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.wanandroid.com/")
                .addConverterFactory(JsonUtil.json.asConverterFactory(contentType))
                .build()
            return retrofit.create(HomePageApi::class.java)
        }
    }

    /**
     * 首页文章列表
     * @param page 页码，从0开始
     */
    @GET("article/list/{page}/json")
    suspend fun getRecommendList(@Path("page") page: Int): DataWrapperModel<PagingDataModel<RecommendDataModel>>
}