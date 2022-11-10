package top.ntutn.wandroidz.api

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import top.ntutn.wandroidz.model.DataWrapperModel
import top.ntutn.wandroidz.model.UserBean

interface UserApi {
    @POST("user/login")
    @FormUrlEncoded
    suspend fun login(@Field("username") username: String, @Field("password") password: String): DataWrapperModel<UserBean>
}