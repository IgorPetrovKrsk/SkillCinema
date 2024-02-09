package igor.petrov.final_android_lvl1.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import igor.petrov.final_android_lvl1.data.WelcomeState
import igor.petrov.final_android_lvl1.domain.KinopoiskUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val kinopoiskUseCase: KinopoiskUseCase) : ViewModel() {
    val _welcomeState = MutableStateFlow<WelcomeState>(WelcomeState.Welcome)
    val welcomeState = _welcomeState.asStateFlow()
    private val dBScope = CoroutineScope( Dispatchers.IO )

    init {
        dBScope.launch {
            kinopoiskUseCase.newCollection("Favorites", true,1)
            kinopoiskUseCase.newCollection("View later", true,2)
            kinopoiskUseCase.newCollection("Viewed", true,3)
            kinopoiskUseCase.newCollection("Interested", true,4) //в этот список добавляем автоматом при открытии фильма или человека
        }
    }
}