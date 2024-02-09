package igor.petrov.final_android_lvl1.data

import igor.petrov.final_android_lvl1.domain.KinopoiskException

sealed class LoadingState {
    data object Ready : LoadingState()
    data object Loading : LoadingState()
    data class Error(val kinopoiskException: KinopoiskException) : LoadingState()
}