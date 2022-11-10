package top.ntutn.wandroidz.account.data

import com.tencent.mmkv.MMKV
import okhttp3.Interceptor
import okhttp3.Response

class CookiesInterceptor: Interceptor {
    companion object {
        private const val KEY_COOKIES = "cookies"
    }

    private val mmkv by lazy {
        MMKV.mmkvWithID("cookies")
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val cookies = mmkv.decodeParcelable(KEY_COOKIES, CookieList::class.java)?.list ?: listOf()
        val requestBuilder = chain.request().newBuilder()
        cookies.forEach {
            requestBuilder.header("Cookie", it.originString.trim())
        }

        val response = chain.proceed(requestBuilder.build())

        val newList = cookies.toMutableList()
        response.headers("Set-Cookie").forEach {
            newList.add(Cookie(it))
        }
        mmkv.encode(KEY_COOKIES, CookieList(newList))

        return response
    }
}