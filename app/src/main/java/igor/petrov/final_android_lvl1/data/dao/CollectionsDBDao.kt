package igor.petrov.final_android_lvl1.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import igor.petrov.final_android_lvl1.data.ItemCollectionType
import igor.petrov.final_android_lvl1.data.dto.collectionDto.CollectionsDBDto
import igor.petrov.final_android_lvl1.data.dto.collectionDto.ItemCollectionDBDto

@Dao
interface CollectionsDBDao {
    @Query("""SELECT CollectionsDB.collectionName,
        CollectionsDB.collectionId,
        CollectionsDB.buildIn,
        COUNT(ItemCollectionDB.collectionId) as collectionSize 
        FROM 
        CollectionsDB 
        LEFT JOIN ItemCollectionDB 
        on CollectionsDB.collectionId = ItemCollectionDB.collectionId 
        WHERE 
        CollectionsDB.collectionId<>3 AND CollectionsDB.collectionId<>4 
        GROUP BY CollectionsDB.collectionName, 
        CollectionsDB.collectionId,CollectionsDB.buildIn""")
    fun selectAll(): List<CollectionsDBDto>

    @Upsert
    fun newCollection(newCollection: CollectionsDBDto):Long

    @Query ("DELETE FROM CollectionsDB WHERE collectionId = :collectionId")
    fun deleteCollection(collectionId:Int)

    @Query ("DELETE FROM ItemCollectionDB WHERE collectionId = :collectionId")
    fun deleteCollectionFromItems(collectionId:Int)

    @Upsert
    fun newItemToCollection(newItemCollection: ItemCollectionDBDto)

    @Query ("SELECT * FROM ItemCollectionDB WHERE collectionId = :collectionId ORDER BY timeUpdated DESC")
    fun getItemCollection(collectionId: Int):List<ItemCollectionDBDto>

    @Query ("SELECT * FROM ItemCollectionDB WHERE itemId = :filmId AND itemType = :filmType")
    fun getFilmCollections(filmId:Int, filmType: ItemCollectionType = ItemCollectionType.FILM): List<ItemCollectionDBDto>

    @Query ("SELECT * FROM ItemCollectionDB WHERE itemId = :filmId AND itemType = :filmType AND collectionId = :collectionId")
    fun isFilmInCollection(filmId: Int,collectionId: Int,filmType: ItemCollectionType = ItemCollectionType.FILM):ItemCollectionDBDto?

    @Query ("DELETE FROM ItemCollectionDB WHERE itemId = :filmId AND itemType = :filmType AND collectionId = :collectionId")
    fun deleteItemFromCollection(filmId: Int,collectionId: Int,filmType: ItemCollectionType = ItemCollectionType.FILM)

}