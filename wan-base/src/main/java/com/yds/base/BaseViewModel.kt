package com.yds.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.crystallake.base.vm.BaseViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

open class BaseViewModel : BaseViewModel() {

    val mStateLiveData = MutableLiveData<State>()

    fun <T> flowEx(method: RequestMethod, block: suspend () -> T) = flow {
        emit(block())
    }.onStart {
        if (method is RequestMethod.Refresh) {
            mStateLiveData.value = State.RefreshState
        } else if (method is RequestMethod.Loading) {
            mStateLiveData.value = State.LoadingState
        } else if (method is RequestMethod.LoadMore) {
            mStateLiveData.value = State.LoadMoreState
        }
    }.onCompletion {
        mStateLiveData.value = State.SuccessState
    }.catch { cause ->
        mStateLiveData.value = State.ErrorState(cause.message)
    }.asLiveData()

}

sealed class State {
    object LoadingState : State()
    object SuccessState : State()
    object LoadMoreState : State()
    object RefreshState : State()
    class ErrorState(val errorMsg: String?) : State()
}

sealed class RequestMethod {
    object Refresh : RequestMethod()
    object LoadMore : RequestMethod()
    object Loading : RequestMethod()
}