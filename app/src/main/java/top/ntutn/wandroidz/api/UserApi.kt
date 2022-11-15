package top.ntutn.wandroidz.api

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import top.ntutn.wandroidz.model.DataWrapperModel
import top.ntutn.wandroidz.model.UserBean
import top.ntutn.wandroidz.model.UserInfoBean

interface UserApi {
    @POST("user/login")
    @FormUrlEncoded
    suspend fun login(@Field("username") username: String, @Field("password") password: String): DataWrapperModel<UserBean>

    @GET("user/lg/userinfo/json")
    suspend fun getUserInfo(): DataWrapperModel<UserInfoBean>
}