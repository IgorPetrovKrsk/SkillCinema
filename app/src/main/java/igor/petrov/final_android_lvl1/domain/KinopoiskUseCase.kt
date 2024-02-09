package igor.petrov.final_android_lvl1.domain


import igor.petrov.final_android_lvl1.data.FilmType
import igor.petrov.final_android_lvl1.data.ItemCollectionType
import igor.petrov.final_android_lvl1.data.OrderType
import igor.petrov.final_android_lvl1.data.PhotoType
import igor.petrov.final_android_lvl1.data.dto.CountryDto
import igor.petrov.final_android_lvl1.data.dto.FilmDto
import igor.petrov.final_android_lvl1.data.dto.GenreDto
import igor.petrov.final_android_lvl1.data.dto.GenresCountriesListDto
import igor.petrov.final_android_lvl1.data.dto.PhotoDto
import igor.petrov.final_android_lvl1.data.dto.StaffDto
import igor.petrov.final_android_lvl1.data.dto.collectionDto.CollectionsDBDto
import igor.petrov.final_android_lvl1.data.dto.collectionDto.ItemCollectionDBDto
import igor.petrov.final_android_lvl1.data.dto.personDto.PersonDto
import igor.petrov.final_android_lvl1.data.dto.serialDto.SerialSeasonsListDto
import igor.petrov.final_android_lvl1.data.repository.KinopoiskRepository
import igor.petrov.final_android_lvl1.data.repository.RoomDBRepository
import igor.petrov.final_android_lvl1.entity.Film
import igor.petrov.final_android_lvl1.entity.collectionDB.CollectionsDB
import igor.petrov.final_android_lvl1.entity.person.Person
import igor.petrov.final_android_lvl1.entity.similarFilm.SimilarFilm
import javax.inject.Inject

class KinopoiskUseCase @Inject constructor(
    private val kinopoiskRepository: KinopoiskRepository,
    private val roomDBRepository: RoomDBRepository
) {
    val favoritesCollection = CollectionsDBDto("Favorites", true, 1) //for later use
    val viewLaterCollection = CollectionsDBDto("View later", true, 2) //for later use
    val viewedCollection = CollectionsDBDto("Viewed", true, 3) //for later use
    val interestedCollection = CollectionsDBDto("Interested", true, 4) //for later use

    suspend fun getFilmPremieresList(): List<Film> {
        val response = kinopoiskRepository.getFilmPremieresList()
        if (response.code() != 200) {
            throw KinopoiskException(response.code(), response.errorBody()?.string())
        } else {
            var tempList = response.body()?.filmList?.subList(0, 20) ?: emptyList<Film>()
            if (tempList.size == 20) tempList =
                tempList.plus(FilmDto(-1, "Show all", "", "", 0, "", "R.drawable.ic_right_arrow", null, null, null, "showAllPremieres")) //таким образом добавим кнопку "показать всё" прямо в recycleView
            return tempList
        }
    }

    suspend fun getAllFilmPremieresList(): List<Film> {
        val response = kinopoiskRepository.getFilmPremieresList()
        if (response.code() != 200) {
            throw KinopoiskException(response.code(), response.errorBody()?.string())
        } else {
            return response.body()?.filmList ?: emptyList<Film>()
        }
    }

    suspend fun getFilmCollectionList(collectionName: String = "TOP_POPULAR_ALL", fullList: Boolean = true, page: Int = 1): List<Film> {
        val response = kinopoiskRepository.getFilmCollectionList(collectionName, page)
        if (response.code() != 200) {
            throw KinopoiskException(response.code(), response.errorBody()?.string())
        } else {
            return if (!fullList) {
                var tempList = response.body()?.filmList ?: emptyList<Film>()
                if (tempList.size == 20) tempList = tempList.plus(FilmDto(-1, "Show all", "", "", 0, "", "R.drawable.ic_right_arrow", null, null, null, collectionName))
                tempList
            } else {
                response.body()?.filmList ?: emptyList<Film>()
            }
        }
    }

    suspend fun getFilmFilterList(page: Int = 1, country: CountryDto?, genre: GenreDto?, order: OrderType? = OrderType.RATING, type: FilmType? = null, ratingFrom: Int = 0, ratingTo: Int = 10, yearFrom: Int = 1000, yearTo: Int = 3000, imdbId: String? = null, keyword: String? = null, fullList: Boolean = true): List<Film> {
        val response =
            kinopoiskRepository.getFilmFilterList(page = page, countryDto = country, genreDto = genre, order = order?.name, type = type?.name, ratingFrom = ratingFrom, ratingTo = ratingTo, yearFrom = yearFrom, yearTo = yearTo, imdbId = imdbId, keyword = keyword)
        if (response.code() != 200) {
            throw KinopoiskException(response.code(), response.errorBody()?.string())
        } else {
            var tempList = response.body()?.filmList ?: emptyList<Film>()

            if (tempList.size == 20 && (!fullList)) tempList =
                tempList.plus(FilmDto(-1, "Show all", "", "", 0, "", "R.drawable.ic_right_arrow", listOf(country!!), listOf(genre!!), null, "showAllFilter", type))
            if (tempList.isEmpty() && (!fullList)) tempList = tempList.plus(FilmDto(-2, "No films", "", "", 0, "", "R.drawable.ic_refresh", listOf(country!!), listOf(genre!!), null, "refresh", type))
            return tempList
        }
    }

    suspend fun getFilm(kinopoiskId: Int, updateInterestedTime: Boolean = false): Film {
        var film = roomDBRepository.getFilm(kinopoiskId)
        if (film == null || System.currentTimeMillis() - (film.timeUpdated ?: 0) > 604800000) { //604800000 неделя в милисекундах, если данным о фильме больше недели то обновим данные РУМе
            val response = kinopoiskRepository.getFilm(kinopoiskId)
            if (response.code() != 200) {
                throw KinopoiskException(response.code(), response.errorBody()?.string())
            } else {
                film = response.body() ?: FilmDto(0)
                if (film.kinopoiskId != 0) {
                    roomDBRepository.newFilm(film)
                    newFilmToCollection(film, interestedCollection)
                }
            }
        }
        if (film.kinopoiskId != 0 && updateInterestedTime) {
            newFilmToCollection(film, interestedCollection)
        }
        return film
    }

    suspend fun getStaff(kinopoiskId: Int): List<StaffDto> {
        val response = kinopoiskRepository.getStaff(kinopoiskId)
        if (response.code() != 200) {
            throw KinopoiskException(response.code(), response.errorBody()?.string())
        } else {
            return response.body() ?: emptyList<StaffDto>()
        }
    }

    suspend fun getGenresCountries(): GenresCountriesListDto {
        val response = kinopoiskRepository.getGenresCountriesList()
        if (response.code() != 200) {
            throw KinopoiskException(response.code(), response.errorBody()?.string())
        } else {
            return response.body() ?: GenresCountriesListDto(emptyList(), emptyList())
        }
    }

    suspend fun getFilmPhotoList(page: Int = 1, kinopoiskId: Int, type: PhotoType?): List<PhotoDto> {
        val response = kinopoiskRepository.getFilmPhotoList(page, kinopoiskId, type)
        if (response.code() != 200) {
            throw KinopoiskException(response.code(), response.errorBody()?.string())
        } else {
            return response.body()?.items ?: emptyList<PhotoDto>()
        }
    }

    suspend fun getSimilarFilmList(kinopoiskId: Int): List<SimilarFilm> {
        val response = kinopoiskRepository.getSimilarFilmList(kinopoiskId)
        if (response.code() != 200) {
            throw KinopoiskException(response.code(), response.errorBody()?.string())
        } else {
            return response.body()?.filmList ?: emptyList<SimilarFilm>()
        }
    }

    suspend fun getPerson(personId: Int, updateInterestedTime: Boolean = false): PersonDto {
        var person = roomDBRepository.getPerson(personId)
        if (person == null || System.currentTimeMillis() - (person.timeUpdated ?: 0) > 604800000) { //604800000 неделя в милисекундах, если данным о человеке больше недели то обновим данные РУМе
            val response = kinopoiskRepository.getPerson(personId)
            if (response.code() != 200) {
                throw KinopoiskException(response.code(), response.errorBody()?.string())
            } else {
                person = response.body() ?: PersonDto(0)
            }
            if (person.personId != 0) {
                roomDBRepository.newPerson(person)
                newPersonToCollection(person, interestedCollection)
            }
        }
        if (person.personId != 0 && updateInterestedTime) {
            newPersonToCollection(person, interestedCollection)
        }
        return person
    }

    suspend fun getSerialSeasonsList(serialId: Int): SerialSeasonsListDto {
        val response = kinopoiskRepository.getSerialSeasonsList(serialId)
        if (response.code() != 200) {
            throw KinopoiskException(response.code(), response.errorBody()?.string())
        } else {
            return response.body() ?: SerialSeasonsListDto(0, emptyList())
        }
    }

    //room DB functions////////////////////////////////////////////////////////////////////////////

    suspend fun newCollection(collectionName: String, collectionBuiltIn: Boolean = false, id: Int = 0): Long {
        return roomDBRepository.newCollection(collectionName, collectionBuiltIn, id)
    }

    suspend fun deleteCollection(collection: CollectionsDB) {
        return roomDBRepository.deleteCollection(collection)
    }

    suspend fun getAllCollections(): List<CollectionsDB> {
        return roomDBRepository.getAllCollections()
    }

    suspend fun newFilm(newFilm: FilmDto) {
        return roomDBRepository.newFilm(newFilm)
    }

    suspend fun newFilmToCollection(film: Film, collection: CollectionsDB) {
        newItemCollection(ItemCollectionDBDto(collection.collectionId, ItemCollectionType.FILM, film.kinopoiskId))
    }

    suspend fun newPersonToCollection(person: Person, collection: CollectionsDB) {
        newItemCollection(ItemCollectionDBDto(collection.collectionId, ItemCollectionType.PERSON, person.personId))
    }

    suspend fun newItemCollection(newItemCollection: ItemCollectionDBDto) {
        return roomDBRepository.newItemCollection(newItemCollection)
    }

    suspend fun getItemCollection(collection: CollectionsDB): List<ItemCollectionDBDto> {
        return roomDBRepository.getItemCollection(collection)
    }

    suspend fun clearCollection(collection: CollectionsDB) {
        return roomDBRepository.clearCollection(collection)
    }

    suspend fun getFilmCollections(filmId: Int): List<ItemCollectionDBDto> {
        return roomDBRepository.getFilmCollections(filmId)
    }

    suspend fun addOrRemoveFilmFromCollection(filmId: Int, collection: CollectionsDB) {
        return roomDBRepository.addOrRemoveFilmFromCollection(filmId, collection)
    }

    suspend fun isFilmViewed(filmId: Int):Boolean{
        return roomDBRepository.isFilmViewed(filmId,viewedCollection)
    }


}