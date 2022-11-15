package top.ntutn.wandroidz.account

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import top.ntutn.wandroidz.api.UserApi
import top.ntutn.wandroidz.model.UserInfoBean
import top.ntutn.wandroidz.util.wanAndroidApi

object AccountService {
    private val userApi by lazy {
        wanAndroidApi<UserApi>()
    }
    private var checkJob: Job? = null
    private var _userInfoData = MutableStateFlow<UserInfoBean?>(null)
    val userInfoData: StateFlow<UserInfoBean?> get() = _userInfoData

    fun startCheckJob() {
        checkJob?.cancel()
        checkJob = GlobalScope.launch {
            while (true) {
                // 最多重试5次
                for (i in 1..5) {
                    try {
                        Timber.i("checking login status, times=%d", i)
                        checkLoginStatus()
                        Timber.i("login check success")
                        break
                    } catch (ignored: Exception) {
                        _userInfoData.value = null
                    }
                }
                delay(60 * 60 * 1000L)
            }
        }
    }

    private suspend fun checkLoginStatus() = withContext(Dispatchers.IO) {
        val userInfo = userApi.getUserInfo()
        require(userInfo.errorCode == 0)
        _userInfoData.value = userInfo.data
    }

    fun login(username: String, password: String) {
        GlobalScope.launch {
            val res = userApi.login(username, password)
            if (res.errorCode == 0) {
                startCheckJob()
            }
        }
    }
}