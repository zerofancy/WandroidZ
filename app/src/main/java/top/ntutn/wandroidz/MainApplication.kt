package top.ntutn.wandroidz

import android.app.Application
import android.content.Context
import android.webkit.WebView
import com.tencent.mmkv.MMKV
import timber.log.Timber
import top.ntutn.wandroidz.account.AccountService

lateinit var appContext: Context

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            WebView.setWebContentsDebuggingEnabled(true)
        }
        val mmkvDir = MMKV.initialize(this)
        Timber.i("mmkv root %s", mmkvDir)

        AccountService.startCheckJob()
    }
}