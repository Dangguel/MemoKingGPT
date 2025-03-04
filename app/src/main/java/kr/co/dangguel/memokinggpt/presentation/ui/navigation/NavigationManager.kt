package kr.co.dangguel.memokinggpt.presentation.navigation

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationManager @Inject constructor() {
    private val _navigationCommands = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val navigationCommands = _navigationCommands.asSharedFlow() // ✅ 외부에서 접근 가능하게 수정

    suspend fun navigate(route: String) {
        _navigationCommands.emit(route)
    }
}
