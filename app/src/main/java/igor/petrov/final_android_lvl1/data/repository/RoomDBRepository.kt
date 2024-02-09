package igor.petrov.final_android_lvl1.data.repository

import igor.petrov.final_android_lvl1.data.ItemCollectionType
import igor.petrov.final_android_lvl1.data.dao.CollectionsDBDao
import igor.petrov.final_android_lvl1.data.dao.FilmDao
import igor.petrov.final_android_lvl1.data.dao.PersonDao
import igor.petrov.final_android_lvl1.data.dto.FilmDto
import igor.petrov.final_android_lvl1.data.dto.collectionDto.CollectionsDBDto
import igor.petrov.final_android_lvl1.data.dto.collectionDto.ItemCollectionDBDto
import igor.petrov.final_android_lvl1.data.dto.personDto.PersonDto
import igor.petrov.final_android_lvl1.entity.collectionDB.CollectionsDB
import javax.inject.Inject

class RoomDBRepository @Inject constructor(private val collectionsDBDao: CollectionsDBDao,private val filmDao: FilmDao,private val personDao: PersonDao) {
    fun getAllCollections(): List<CollectionsDBDto> {
        return collectionsDBDao.selectAll()
    }

    fun newCollection(collectionName:String,collectionBuiltIn:Boolean = false, id:Int = 0):Long {
        return collectionsDBDao.newCollection(CollectionsDBDto(collectionName,collectionBuiltIn,id))
    }

    fun deleteCollection(collection:CollectionsDB) {
        collectionsDBDao.deleteCollection(collection.collectionId)
        collectionsDBDao.deleteCollectionFromItems(collection.collectionId)
    }

    fun getFilm(kinopoiskId: Int): FilmDto?{
        return filmDao.getFilm(kinopoiskId)
    }

    fun newFilm(film: FilmDto){
        return filmDao.newFilm(film)
    }

    fun getPerson(personId: Int): PersonDto?{
        return personDao.getPerson(personId)
    }

    fun newPerson(person: PersonDto){
        return personDao.newPerson(person)
    }

    fun newItemCollection(newItemCollection: ItemCollectionDBDto){
        return collectionsDBDao.newItemToCollection(newItemCollection)
    }

    fun getItemCollection(collection: CollectionsDB):List<ItemCollectionDBDto>{
        return collectionsDBDao.getItemCollection(collection.collectionId)
    }

    fun clearCollection(collection:CollectionsDB) {
        collectionsDBDao.deleteCollectionFromItems(collection.collectionId)
    }

    fun getFilmCollections (filmId:Int):List<ItemCollectionDBDto>{
        return collectionsDBDao.getFilmCollections(filmId)
    }

    fun addOrRemoveFilmFromCollection(filmId: Int,collection: CollectionsDB){
        if (collectionsDBDao.isFilmInCollection(filmId,collection.collectionId) == null){
            return collectionsDBDao.newItemToCollection(ItemCollectionDBDto(collection.collectionId,ItemCollectionType.FILM,filmId))
        }else{
            return collectionsDBDao.deleteItemFromCollection(filmId,collection.collectionId)
        }
    }

    fun isFilmViewed(filmId: Int,viewedCollection:CollectionsDB):Boolean{
        return (collectionsDBDao.isFilmInCollection(filmId,viewedCollection.collectionId) != null)
    }
}