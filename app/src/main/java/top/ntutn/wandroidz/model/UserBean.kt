package top.ntutn.wandroidz.model

/**
 * {
 *     "data": {
 *         "admin": false,
 *         "chapterTops": [],
 *         "coinCount": 21,
 *         "collectIds": [],
 *         "email": "ntutn.top@gmail.com",
 *         "icon": "",
 *         "id": 142162,
 *         "nickname": "Payphone1881",
 *         "password": "",
 *         "publicName": "Payphone1881",
 *         "token": "",
 *         "type": 0,
 *         "username": "Payphone1881"
 *     },
 *     "errorCode": 0,
 *     "errorMsg": ""
 * }
 */
@kotlinx.serialization.Serializable
class UserBean(
    val id: Long,
    val nickname: String? = null,
    val username: String? = null,
    val admin: Boolean = false,
    val coinCount: Int = 0,
    val email: String? = null
)
