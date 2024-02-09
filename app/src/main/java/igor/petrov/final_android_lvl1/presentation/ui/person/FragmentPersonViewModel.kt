package igor.petrov.final_android_lvl1.presentation.ui.person

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import igor.petrov.final_android_lvl1.data.LoadingState
import igor.petrov.final_android_lvl1.data.dto.personDto.PersonDto
import igor.petrov.final_android_lvl1.domain.KinopoiskException
import igor.petrov.final_android_lvl1.domain.KinopoiskUseCase
import igor.petrov.final_android_lvl1.entity.Film
import igor.petrov.final_android_lvl1.entity.person.Person
import igor.petrov.final_android_lvl1.entity.person.PersonFilm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentPersonViewModel @Inject constructor(private val kinopoiskUseCase: KinopoiskUseCase) : ViewModel() {

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Loading)
    val loadingState = _loadingState.asStateFlow()

    private val _person = MutableStateFlow<Person>(PersonDto(0))
    var person: StateFlow<Person> = _person.asStateFlow()
    var personProfessionKeyList: List<String>? = emptyList()
    private val dbScope = CoroutineScope(Dispatchers.IO)

    var personId: Int = 0
    fun getPerson() {
        _loadingState.value = LoadingState.Loading
        dbScope.launch {
            try {
                _person.value = kinopoiskUseCase.getPerson(personId, true)
                personProfessionKeyList = _person.value.films?.map { it.professionKey ?: "Other" }?.distinct()
                _loadingState.value = LoadingState.Ready
            } catch (e: KinopoiskException){
                _loadingState.value = LoadingState.Error(e)
            }

        }
    }

    suspend fun getFilmDetails(personFilm: PersonFilm): Film {
        return kinopoiskUseCase.getFilm(personFilm.filmId)
    }


}