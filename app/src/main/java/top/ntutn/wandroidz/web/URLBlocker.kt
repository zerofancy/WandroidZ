package top.ntutn.wandroidz.web

abstract class URLBlocker(private val nextBlocker: URLBlocker?) {
    enum class Result {
        PASS,
        BLOCK
    }

    fun check(url: String?): Result {
        return handle(url)
            .takeIf { it != Result.PASS }
            ?: nextBlocker?.check(url)
            ?: Result.PASS
    }

    protected abstract fun handle(url: String?): Result
}