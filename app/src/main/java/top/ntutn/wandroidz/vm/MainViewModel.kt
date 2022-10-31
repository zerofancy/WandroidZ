package top.ntutn.wandroidz.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.ntutn.wandroidz.IMainListItem
import top.ntutn.wandroidz.api.HomePageApi
import top.ntutn.wandroidz.model.RecommendDataModel

class MainViewModel: ViewModel() {
    enum class State {
        IDLE,
        LOADING
    }

    private val _currentState = MutableStateFlow(State.IDLE)
    val currentState: StateFlow<State> get() = _currentState

    private val _datas = MutableStateFlow(listOf<IMainListItem>())
    val datas: MutableStateFlow<List<IMainListItem>> get() = _datas

    private var currentPage = 0

    fun refresh() {
        viewModelScope.launch {
            currentPage = 0
            val result: MutableList<IMainListItem> = load().map { IMainListItem.NormalItem(it) }.toMutableList()
            result.add(IMainListItem.FooterItem())
            _datas.value = result
            currentPage++
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            val originList = datas.value.filter { it !is IMainListItem.FooterItem }.toMutableList()
            val result = load().toMutableList().map { IMainListItem.NormalItem(it) }
            originList.addAll(result)
            originList.add(IMainListItem.FooterItem())
            _datas.value = originList
            currentPage++
        }
    }

    private suspend fun load(): List<RecommendDataModel> {
        if (currentState.value != State.IDLE) {
            return emptyList() // fixme move to caller
        }
        _currentState.value = State.LOADING
        val result = withContext(Dispatchers.IO) {
            HomePageApi.get().getRecommendList(currentPage)
        }
        _currentState.value = State.IDLE
        if (result.errorCode == 0) {
            return result.data.datas
        }
        return emptyList()
    }
}