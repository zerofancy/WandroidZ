package top.ntutn.wandroidz.account.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import top.ntutn.wandroidz.account.data.model.LoggedInUser
import top.ntutn.wandroidz.api.UserApi
import top.ntutn.wandroidz.util.wanAndroidApi
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    private val api = wanAndroidApi<UserApi>()

    suspend fun login(username: String, password: String): Result<LoggedInUser> = withContext(Dispatchers.IO) {
        try {
            val response = api.login(username, password)
            require(response.errorCode == 0) { response.errorMessage }
            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), response.data.nickname ?: "")
            return@withContext Result.Success(fakeUser)
        } catch (e: Throwable) {
            return@withContext Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}