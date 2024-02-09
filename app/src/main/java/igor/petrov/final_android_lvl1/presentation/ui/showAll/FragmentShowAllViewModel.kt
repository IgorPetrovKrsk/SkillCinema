package igor.petrov.final_android_lvl1.presentation.ui.showAll

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import dagger.hilt.android.lifecycle.HiltViewModel
import igor.petrov.final_android_lvl1.App
import igor.petrov.final_android_lvl1.R
import igor.petrov.final_android_lvl1.data.FilmType
import igor.petrov.final_android_lvl1.data.ItemCollectionType
import igor.petrov.final_android_lvl1.data.LoadingState
import igor.petrov.final_android_lvl1.data.OrderType
import igor.petrov.final_android_lvl1.data.dto.CountryDto
import igor.petrov.final_android_lvl1.data.dto.GenreDto
import igor.petrov.final_android_lvl1.data.dto.StaffDto
import igor.petrov.final_android_lvl1.data.dto.collectionDto.CollectionsDBDto
import igor.petrov.final_android_lvl1.data.dto.collectionDto.ItemCollectionDBDto
import igor.petrov.final_android_lvl1.data.dto.collectionDto.ItemCollectionForRecyclerView
import igor.petrov.final_android_lvl1.data.dto.personDto.PersonDto
import igor.petrov.final_android_lvl1.data.dto.similarFilmDto.SimilarFilmDto
import igor.petrov.final_android_lvl1.domain.KinopoiskException
import igor.petrov.final_android_lvl1.domain.KinopoiskUseCase
import igor.petrov.final_android_lvl1.entity.Film
import igor.petrov.final_android_lvl1.entity.Staff
import igor.petrov.final_android_lvl1.entity.person.Person
import igor.petrov.final_android_lvl1.entity.person.PersonFilm
import igor.petrov.final_android_lvl1.entity.similarFilm.SimilarFilm
import igor.petrov.final_android_lvl1.presentation.ui.home.FilmCollectionListPagingSource
import igor.petrov.final_android_lvl1.presentation.ui.home.FilmFilterListPagingSource
import igor.petrov.final_android_lvl1.presentation.ui.home.FilmPremieresListPagingSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalStdlibApi::class)
@HiltViewModel
class FragmentShowAllViewModel @Inject constructor(private val kinopoiskUseCase: KinopoiskUseCase) :
    ViewModel() {

    private val dbScope = CoroutineScope(Dispatchers.IO)
    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Ready)
    val loadingState = _loadingState.asStateFlow()


    private val _person = MutableStateFlow<Person>(PersonDto(0))
    var person: StateFlow<Person> = _person.asStateFlow()
    var personProfessionKeyList: List<String>? = emptyList()
    var personId: Int = 0


    private val _collectionFilmList = MutableStateFlow<List<ItemCollectionForRecyclerView>>(emptyList())
    val collectionFilmList = _collectionFilmList.asStateFlow()

    private val _staffFromJson = MutableStateFlow<List<Staff>?>(emptyList())
    val staffFromJson = _staffFromJson.asStateFlow()

    private val _similarFilmFromJson = MutableStateFlow<List<SimilarFilm>?>(emptyList())
    val similarFilmFromJson = _similarFilmFromJson.asStateFlow()



    var showType: String? = null
    var label: String? = null
    var collection: String? = null
    var collectionId: Int? = null
    var json: String? = null

    var countries: Int? = null
    var genres: Int? = null
    var order: OrderType? = null
    var type: FilmType? = null
    var ratingFrom: Int = 0
    var ratingTo: Int = 10
    var yearFrom: Int = 1000
    var yearTo: Int = 3000
    var imdbId: String? = null
    var keyword: String? = null

    var showAllFilmList: Flow<PagingData<Film>> = emptyFlow()

    fun getCollection() {
        when (showType) {
            "premieres" -> {
                showAllFilmList = Pager(
                    config = PagingConfig(pageSize = 20),
                    initialKey = null,
                    pagingSourceFactory = { FilmPremieresListPagingSource(kinopoiskUseCase, true) }
                ).flow.cachedIn(viewModelScope)
            }

            "collection" -> {
                showAllFilmList = Pager(
                    config = PagingConfig(pageSize = 20),
                    initialKey = null,
                    pagingSourceFactory = {
                        FilmCollectionListPagingSource(
                            kinopoiskUseCase,
                            collection ?: "TOP_POPULAR_ALL",
                            true
                        )
                    }
                ).flow.cachedIn(viewModelScope)
            }

            "filter" -> {
                showAllFilmList = Pager(
                    config = PagingConfig(pageSize = 20),
                    initialKey = null,
                    pagingSourceFactory = {
                        FilmFilterListPagingSource(
                            kinopoiskUseCase,
                            CountryDto(countries, ""),
                            GenreDto(genres, ""),
                            order,
                            type,
                            ratingFrom,
                            ratingTo,
                            yearFrom,
                            yearTo,
                            imdbId,
                            keyword,
                            true
                        )
                    }
                ).flow.cachedIn(viewModelScope)

            }
        }
    }

    fun getCollectionFilm(fullList: Boolean = true) {
        _loadingState.value = LoadingState.Loading
        dbScope.launch {
            val itemCollectionForRecyclerViewList = mutableListOf<ItemCollectionForRecyclerView>()
            var itemCollectionList: List<ItemCollectionDBDto> = emptyList()
            try {
                itemCollectionList = kinopoiskUseCase.getItemCollection(CollectionsDBDto("",false,collectionId?:0))
            } catch (e: KinopoiskException) {
                _loadingState.value = LoadingState.Error(e)
            }
            if (!fullList) {
                itemCollectionList = itemCollectionList.take(20)
            }
            itemCollectionList.forEach {
                val itemCollectionForRecyclerView = ItemCollectionForRecyclerView(it.collectionId, it.itemType, it.itemId, it.timeUpdated)
                when (it.itemType) {
                    ItemCollectionType.FILM -> {
                        val film = kinopoiskUseCase.getFilm(it.itemId)
                        itemCollectionForRecyclerView.itemName = if (film.nameRu.isNullOrBlank()) film.nameEn else film.nameRu
                        itemCollectionForRecyclerView.itemDescription = film.genres?.joinToString()
                        itemCollectionForRecyclerView.itemRating = film.ratingKinopoisk
                        itemCollectionForRecyclerView.posterUrl = film.posterUrlPreview
                    }

                    ItemCollectionType.PERSON -> {
                        val person = kinopoiskUseCase.getPerson(it.itemId)
                        itemCollectionForRecyclerView.itemName = if (person.nameRu.isNullOrBlank()) person.nameEn else person.nameRu
                        itemCollectionForRecyclerView.itemDescription = "${person.profession ?: "---"}"
                        itemCollectionForRecyclerView.itemRating = null
                        itemCollectionForRecyclerView.posterUrl = person.posterUrl
                    }
                }
                itemCollectionForRecyclerViewList.add(itemCollectionForRecyclerView)
            }
            if (!fullList) {
                val itemCollectionForRecyclerView = ItemCollectionForRecyclerView(kinopoiskUseCase.interestedCollection.collectionId, ItemCollectionType.FILM, 0, System.currentTimeMillis())
                itemCollectionForRecyclerView.itemName = App.applicationContext().getString(R.string.text_clear_history)
                itemCollectionForRecyclerView.itemDescription = ""
                itemCollectionForRecyclerView.itemRating = null
                itemCollectionForRecyclerView.posterUrl = R.drawable.ic_trash.toString()
                itemCollectionForRecyclerViewList.add(itemCollectionForRecyclerView)
            }
            _collectionFilmList.value = itemCollectionForRecyclerViewList
            _loadingState.value = LoadingState.Ready
        }
    }

    fun getStaffFromJson(){
        _loadingState.value = LoadingState.Loading
        dbScope.launch {
            val moshi: Moshi = Moshi.Builder().build()
            val jsonAdapter: JsonAdapter<List<StaffDto>> = moshi.adapter<List<StaffDto>>()
            _staffFromJson.value = jsonAdapter.fromJson(json?:"")
            _loadingState.value = LoadingState.Ready
        }

    }

    fun getSimilarFilmFromJson(){
        _loadingState.value = LoadingState.Loading
        dbScope.launch {
            val moshi: Moshi = Moshi.Builder().build()
            val jsonAdapter: JsonAdapter<List<SimilarFilmDto>> = moshi.adapter<List<SimilarFilmDto>>()
            _similarFilmFromJson.value = jsonAdapter.fromJson(json?:"")
            _loadingState.value = LoadingState.Ready
        }

    }


    fun getPerson() {
        dbScope.launch {
            _loadingState.value = LoadingState.Loading
            try {
                _person.value = kinopoiskUseCase.getPerson(personId, true)
                personProfessionKeyList = _person.value.films?.map { it.professionKey ?: "Other" }
                _loadingState.value = LoadingState.Ready
            }catch (e: KinopoiskException){
                _loadingState.value = LoadingState.Error(e)
            }

        }
    }

    suspend fun getFilmDetails(personFilm: PersonFilm): Film {
        return kinopoiskUseCase.getFilm(personFilm.filmId)
    }

    suspend fun isFilmViewed(film: Film):Boolean{
        return kinopoiskUseCase.isFilmViewed(film.kinopoiskId)
    }
}
