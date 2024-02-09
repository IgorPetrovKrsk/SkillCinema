package igor.petrov.final_android_lvl1.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import igor.petrov.final_android_lvl1.data.dao.CollectionsDBDao
import igor.petrov.final_android_lvl1.data.dao.FilmDao
import igor.petrov.final_android_lvl1.data.dao.PersonDao
import igor.petrov.final_android_lvl1.data.dto.FilmDto
import igor.petrov.final_android_lvl1.data.dto.collectionDto.CollectionsDBDto
import igor.petrov.final_android_lvl1.data.dto.collectionDto.ItemCollectionDBDto
import igor.petrov.final_android_lvl1.data.dto.personDto.PersonDto

@Database(entities = [
    CollectionsDBDto::class,
    ItemCollectionDBDto::class,
    FilmDto::class,
    PersonDto::class
], version = 8,
    exportSchema = false)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun collectionsDBDao(): CollectionsDBDao
    abstract fun filmDao(): FilmDao
    abstract fun personDao(): PersonDao
}