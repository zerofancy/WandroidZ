package top.ntutn.wandroidz.util

import android.content.res.Resources
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import top.ntutn.wandroidz.account.data.CookiesInterceptor

val Int.dp: Int
    get() {
        val dpValue = this
        return (0.5f + dpValue * Resources.getSystem().displayMetrics.density).toInt()
    }

val Float.dpFloat: Float
    get() {
        val dpValue = this
        return dpValue * Resources.getSystem().displayMetrics.density
    }

val Int.sp: Int
    get() {
        val dpValue = this
        return (0.5f + dpValue * Resources.getSystem().displayMetrics.scaledDensity).toInt()
    }

val Float.spFloat: Float
    get() {
        val dpValue = this
        return dpValue * Resources.getSystem().displayMetrics.scaledDensity
    }

val okHttpClient: OkHttpClient by lazy {
    OkHttpClient.Builder()
        .addInterceptor(CookiesInterceptor())
        .build()
}

inline fun <reified T> wanAndroidApi(): T {
    val contentType = MediaType.get("application/json")
    val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://www.wanandroid.com/")
        .addConverterFactory(JsonUtil.json.asConverterFactory(contentType))
        .build()
    return retrofit.create(T::class.java)
}