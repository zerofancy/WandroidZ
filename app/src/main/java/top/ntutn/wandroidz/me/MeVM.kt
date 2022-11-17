package top.ntutn.wandroidz.me

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import top.ntutn.wandroidz.account.AccountService
import top.ntutn.wandroidz.me.data.AvatarData
import top.ntutn.wandroidz.me.data.IMeListItemData

class MeVM: ViewModel() {
    private val _mainList = MutableStateFlow(emptyList<IMeListItemData>())
    val mainList: Flow<List<IMeListItemData>> get() = _mainList

    fun init() {
        AccountService.userInfoData.onEach {
            val list = _mainList.value.toMutableList()
            val avatarData = list.find { it is AvatarData }
            list.remove(avatarData)
            list.add(0, AvatarData(it))
            _mainList.value = list
        }.launchIn(viewModelScope)
    }
}