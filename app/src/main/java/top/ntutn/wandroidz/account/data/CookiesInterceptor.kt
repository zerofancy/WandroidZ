package top.ntutn.wandroidz.account.data

import com.tencent.mmkv.MMKV
import okhttp3.Interceptor
import okhttp3.Response
import top.ntutn.wandroidz.util.okHttpCookie
import top.ntutn.wandroidz.util.toCookie

class CookiesInterceptor : Interceptor {
    companion object {
        private const val KEY_COOKIES = "cookies"
    }

    private val mmkv by lazy {
        MMKV.mmkvWithID("cookies")
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val cookies = mmkv.decodeParcelable(KEY_COOKIES, CookieList::class.java)?.list ?: listOf()
        val originRequest = chain.request()
        val requestBuilder = originRequest.newBuilder()
        cookies
            .map { it.okHttpCookie() }
            .joinToString(";")
            .let {
                requestBuilder.header("Cookie", it)
            }

        val response = chain.proceed(requestBuilder.build())


        val cookieMap = mutableMapOf<String, okhttp3.Cookie>()
        cookies.forEach {
            cookieMap.put(it.name, it.okHttpCookie())
        }

        response.headers("Set-Cookie").forEach {
            okhttp3.Cookie.parse(originRequest.url(), it)?.let {
                cookieMap.put(it.name(), it)
            }
        }
        mmkv.encode(KEY_COOKIES, CookieList(cookieMap.map { it.value.toCookie() }))
        return response
    }
}