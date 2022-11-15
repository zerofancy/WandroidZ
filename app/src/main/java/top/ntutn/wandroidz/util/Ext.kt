package top.ntutn.wandroidz.util

import android.content.res.Resources
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import top.ntutn.wandroidz.account.data.Cookie
import top.ntutn.wandroidz.appContext

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
//        .addInterceptor(CookiesInterceptor())
        .cookieJar(PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(appContext))) // https://github.com/franmontiel/PersistentCookieJar
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

fun Cookie.okHttpCookie(): okhttp3.Cookie {
    val builder = okhttp3.Cookie
        .Builder()
        .name(name)
    value?.let {
        builder.value(it)
    }
    expiresAt?.let {
        builder.expiresAt(it)
    }
    path?.let {
        builder.path(it)
    }
    if (secure) {
        builder.secure()
    }
    if (hostOnly) {
        builder.httpOnly()
    }
    if (hostOnly) {
        domain?.let {
            builder.hostOnlyDomain(domain)
        }
    } else {
        domain?.let {
            builder.domain(domain)
        }
    }
    return builder.build()
}

fun okhttp3.Cookie.toCookie(): Cookie {
    return Cookie(
        name(), value(), expiresAt(), domain(), path(), secure(), httpOnly(), hostOnly()
    )
}