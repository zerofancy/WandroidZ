package top.ntutn.wandroidz.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.ntutn.wandroidz.api.HomePageApi
import top.ntutn.wandroidz.model.RecommendDataModel

class MainViewModel: ViewModel() {
    enum class State {
        IDLE,
        LOADING
    }

    private val _currentState = MutableStateFlow(State.IDLE)
    val currentState: StateFlow<State> get() = _currentState

    private val _datas = MutableStateFlow(listOf<RecommendDataModel>())
    val datas: MutableStateFlow<List<RecommendDataModel>> get() = _datas

    private var currentPage = 0

    fun refresh() {
        viewModelScope.launch {
            currentPage = 0
            val result = load()
            _datas.value = result
            currentPage++
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            val result = load().toMutableList()
            result.addAll(0, datas.value)
            _datas.value = result
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