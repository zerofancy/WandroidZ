package top.ntutn.wandroidz.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import top.ntutn.wandroidz.mainpage.IMainListItem
import top.ntutn.wandroidz.mainpage.BannerDataSource
import top.ntutn.wandroidz.mainpage.RecommendDataSource

class MainViewModel: ViewModel() {
    enum class State {
        REFRESHING,
        IDLE,
        LOADING
    }

    private val bannerDataSource = BannerDataSource()
    private val recommendDataSource = RecommendDataSource()

    private val _currentState = MutableStateFlow(State.IDLE)
    val currentState: StateFlow<State> get() = _currentState

    private val _datas = MutableStateFlow(listOf<IMainListItem>())
    val datas: MutableStateFlow<List<IMainListItem>> get() = _datas

    private var currentPage = 0

    fun refresh() {
        viewModelScope.launch {
            _currentState.value = State.REFRESHING

            // load banner
            val result: MutableList<IMainListItem> = bannerDataSource.getBannerData()
                .takeIf { it.isNotEmpty() }
                ?.let { IMainListItem.BannerItem(it.random()) }
                ?.let { listOf(it) }
                ?.toMutableList() ?: mutableListOf()

            // load content
            currentPage = 0
            result.add(IMainListItem.FooterItem())
            _datas.value = result

            _currentState.value = State.IDLE
        }
    }

    fun loadMore() {
        if (currentState.value != State.IDLE) {
            return
        }
        viewModelScope.launch {
            _currentState.value = State.LOADING

            val loaded = recommendDataSource.load(currentPage++)
                .map { IMainListItem.NormalItem(it) }
            val originList = datas.value
                .filter { it !is IMainListItem.FooterItem }
                .toMutableList()
            originList.addAll(originList.size, loaded)
            originList.add(IMainListItem.FooterItem())
            _datas.value = originList

            _currentState.value = State.IDLE
        }
    }
}