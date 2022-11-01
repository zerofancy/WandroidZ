package top.ntutn.wandroidz

import android.app.Application
import com.tencent.mmkv.MMKV
import timber.log.Timber

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        val mmkvDir = MMKV.initialize(this)
        Timber.i("mmkv root %s", mmkvDir)
    }
}