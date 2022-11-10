package top.ntutn.wandroidz.account.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cookie(
    var expires: String? = null,
    var maxAge: Long? = null,
    var domain: String? = null,
    var originString: String = ""
) : Parcelable {
    constructor(cookieString: String) : this(originString = cookieString)

    init {
        val splits = originString.split(';')
        val map = buildMap {
            splits.forEach {
                it.split('=').let {
                    put(it.first().lowercase().trim(), it.last().trim())
                }
            }
        }
        expires = map["expires"]
        maxAge = map["max-age"]?.toLong()
        domain = map["domain"]
    }
}

@Parcelize
data class CookieList(val list: List<Cookie>): Parcelable