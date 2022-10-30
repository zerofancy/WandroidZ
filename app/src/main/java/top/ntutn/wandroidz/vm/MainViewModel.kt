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

    fun init() {
        loadMore()
    }

    fun loadMore() {
        if (currentState.value != State.IDLE) {
            return
        }
        _currentState.value = State.LOADING

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                HomePageApi.get().getRecommendList(currentPage)
            }
            if (result.errorCode == 0) {
                val list = result.data.datas.toMutableList()
                list.addAll(0, _datas.value)
                _datas.value = list
            }
        }
    }
}