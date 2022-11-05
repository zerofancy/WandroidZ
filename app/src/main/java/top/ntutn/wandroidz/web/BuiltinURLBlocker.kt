package top.ntutn.wandroidz.web

import android.net.Uri

class BuiltinURLBlocker(nextURLBlocker: URLBlocker? = null): URLBlocker(nextURLBlocker) {
    private val blockDomainList = listOf("jq.qq.com") // 大佬不要打我，手机上WebView打不开这个加群地址的

    override fun handle(url: String?): Result {
        if (url.isNullOrBlank()) {
            return Result.BLOCK
        }
        val uri = Uri.parse(url)
        if (uri.authority in blockDomainList) {
            return Result.BLOCK
        }
        return Result.PASS
    }
}