package top.ntutn.wandroidz.account.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cookie(
    val name: String = "",
    val value: String? = null,
    val expiresAt: Long? = null,
    val domain: String? = null,
    val path: String? = null,
    val secure: Boolean = false,
    val httpOnly: Boolean = false,
    val hostOnly: Boolean = false,
) : Parcelable

@Parcelize
data class CookieList(val list: List<Cookie>): Parcelable